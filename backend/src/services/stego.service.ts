import pool from "../config/database.ts"

export const getStegoLogsService = async () => {
    const dbClient = await pool.connect() //TODO: turn this to middleware when auth is implemented
    try {
        const result = await dbClient.query(`SELECT stego_log.*, effects.name as effect_name
            FROM stego_log
            INNER JOIN effects ON effect_id = effects.id`)
        const stegoLogList = result.rows

        const formattedLog = stegoLogList.map((logItem) => ({
            id: logItem.id,
            user_id: logItem.user_id,
            effect_id: logItem.effect_id,
            effect_name: logItem.effect_name,
            type: logItem.type,
            time_taken: logItem.time_taken,
            created_at: logItem.created_at
        }))
        return {
            success: true,
            message: "Get stego log service successful",
            data: formattedLog
        }
    } catch (error) {
        return {
            success: false,
            message: "Get stego log service error",
            error
        }
    } finally {
        dbClient.release()
    }
}

export const getStegoLogByIdService = async (id: string) => {
    const dbClient = await pool.connect() //TODO: turn this to middleware when auth is implemented
    try {
        const result = await dbClient.query(`SELECT stego_log.*, effects.name as effect_name
            FROM stego_log
            INNER JOIN effects ON effect_id = effects.id
            WHERE id = $1`, [id])
        const stegoLogList = result.rows[0]

        const formattedLog = {
            id: stegoLogList.id,
            user_id: stegoLogList.user_id,
            effect_id: stegoLogList.effect_id,
            effect_name: stegoLogList.effect_name,
            type: stegoLogList.type,
            time_taken: stegoLogList.time_taken,
            created_at: stegoLogList.created_at
        }
        if (result.rowCount == 0) {
            return {
                success: false,
                message: "Error fetching stego log",
                error: "Stego log does not exist or unauthorized fetch"
            }
        }

        return {
            success: true,
            message: "Get stego log service successful",
            data: formattedLog
        }
    } catch (error) {
        return {
            success: false,
            message: "Get stego log service error",
            error
        }
    } finally {
        dbClient.release()
    }
}

export const createStegoLogService = async (reqBody: any) => {
    const dbClient = await pool.connect() //TODO: turn this to middleware when auth is implemented
    try {
        const { user_id, effect_name, type, time_taken } = reqBody
        const effectsResult = await dbClient.query(`SELECT * FROM effects WHERE name = $1`, [effect_name])
        if (effectsResult.rowCount == 0) {
            return {
                success: false,
                message: "Effect not found",
                error: "Effect not found"
            }
        }
        const effect_id = effectsResult.rows[0].id
        const result = await dbClient.query(`INSERT INTO stego_log (user_id, effect_id, type, time_taken) VALUES ($1, $2, $3, $4) RETURNING *`, 
            [user_id, effect_id, type, time_taken])

        const createdStegoLog = result.rows[0]

        if (result.rowCount == 0) {
            return {
                success: false,
                message: "Error creating stego log",
                error: "Error creating stego log"
            }
        }

        return {
            success: true,
            message: "Create stego log service successful",
            data: createdStegoLog
        }
    } catch (error) {
        return {
            success: false,
            message: "Create stego log service error",
            error
        }
    } finally {
        dbClient.release()
    }
}

export const deleteStegoLogService = async (id: string) => {
    const dbClient = await pool.connect() //TODO: turn this to middleware when auth is implemented
    try {
        const result = await dbClient.query(`DELETE FROM stego_log WHERE id = $1`, [id])

        const deletedStegoLog = result.rows[0]

        if (result.rowCount == 0) {
            return {
                success: false,
                message: "Error deleting stego log",
                error: "Stego Log not found"
            }
        }

        return {
            success: true,
            message: "Delete stego log service successful",
            data: deletedStegoLog
        }
    } catch (error) {
        return {
            success: false,
            message: "Delete stego log service error",
            error
        }
    } finally {
        dbClient.release()
    }
}