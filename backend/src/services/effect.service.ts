import type { PoolClient } from "pg"

export const getEffectsService = async (dbClient: PoolClient) => {
    try {
        const result = await dbClient.query(`SELECT * FROM effects`)
        const effectList = result.rows

        const formattedEffects = effectList.map((effect) => ({
            id: effect.id,
            name: effect.name,
            avg_embed_time: effect.avg_embed_time,
            avg_extract_time: effect.avg_extract_time,
            total_embed_usage: effect.total_embed_usage,
            total_extract_usage: effect.total_extract_usage,
            avg_results: effect.avg_results,
            created_at: effect.created_at
        }))
        return {
            success: true,
            message: "Get effect service successful",
            data: formattedEffects
        }
    } catch (error) {
        return {
            success: false,
            message: "Get effect service error",
            error
        }
    }
}

export const createEffectService = async (dbClient: PoolClient, reqBody: any) => {
    try {
        const { name } = reqBody
        const result = await dbClient.query(`INSERT INTO effects (name) VALUES ($1) RETURNING *`, [name])
        const effect = result.rows[0]

        if (result.rowCount === 0) {
            return {
                success: false,
                message: "Error creating effect",
                error: "Error creating effect"
            }
        }
        return {
            success: true,
            message: "Create effect service successful",
            data: effect
        }
    } catch (error) {
        return {
            success: false,
            message: "Create effect service error",
            error
        }
    }
}

export const updateEffectService = async (dbClient: PoolClient, id: string, reqBody: any) => {
    try {
        const { name } = reqBody
        const result = await dbClient.query(`UPDATE effects SET name = $1 WHERE id = $2 RETURNING *`, [name, id])
        const effect = result.rows[0]

        if (result.rowCount === 0) {
            return {
                success: false,
                message: "Error updating effect",
                error: "Effect does not exist"
            }
        }
        return {
            success: true,
            message: "Update effect service successful",
            data: effect
        }
    } catch (error) {
        return {
            success: false,
            message: "Update effect service error",
            error
        }
    }
}

export const deleteEffectService = async (dbClient: PoolClient, id: string) => {
    try {
        const result = await dbClient.query(`DELETE FROM effects WHERE id = $1 RETURNING *`, [id])
        const effect = result.rows[0]

        if (result.rowCount === 0) {
            return {
                success: false,
                message: "Error deleting effect",
                error: "Effect not found"
            }
        }
        return {
            success: true,
            message: "Delete effect service successful",
            data: effect
        }
    } catch (error) {
        return {
            success: false,
            message: "Delete effect service error",
            error
        }
    }
}