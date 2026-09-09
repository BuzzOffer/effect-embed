import type { PoolClient } from "pg";
import type { SignedUserInfo } from "../types/auth.ts";
import { pool } from "../config/database.ts";

export const queryWithRls = async <T, Args extends any[]>(user: SignedUserInfo, callback: (client: PoolClient, ...args: Args) => Promise<T>, ...args: Args): Promise<T> => {
    const client = await pool.connect();
    try {
        await client.query('BEGIN');

        //pass user context
        await client.query(`SELECT set_config('app.current_user_id', $1, true)`, [user.id]);
        await client.query(`SELECT set_config('app.current_role', $1, true)`, [user.role]);

        //in this context, callback is the service function
        const result = await callback(client, ...args);

        await client.query('COMMIT');
        return result;
    } catch (error) {
        await client.query('ROLLBACK');
        throw error;
    } finally {
        client.release()
    }
}