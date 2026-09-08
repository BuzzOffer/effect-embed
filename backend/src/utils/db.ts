import type { PoolClient } from "pg";
import type { SignedUserInfo } from "../types/auth.ts";
import pool from "../config/database.ts";

export const queryWithRls = async <T>(user: SignedUserInfo, callback: (client: PoolClient) => Promise<T>): Promise<T> => {
    const client = await pool.connect();
    try {
        await client.query('BEGIN');

        //pass user context
        await client.query(`SELECT set_config('app.current_user_id', $1, true)`, [user.id]);
        await client.query(`SELECT set_config('app.current_role', $1, true)`, [user.role]);

        //in this context, callback is the service function
        const result = await callback(client);

        await client.query('COMMIT');
        return result;
    } catch (error) {
        await client.query('ROLLBACK');
        throw error;
    } finally {
        client.release()
    }
}