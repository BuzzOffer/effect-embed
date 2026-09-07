import type { Request, Response } from "express";
import { createEffectService, deleteEffectService, getEffectsService, updateEffectService } from "../services/effect.service.ts";
import { errorResponse, successResponse } from "../utils/response.ts";

export const getEffects = async (req: Request, res: Response) => {
    try {
        const result = await getEffectsService()
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { effects: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Get effects controller error", 400, error)
    }
}

export const createEffect = async (req: Request, res: Response) => {
    try {
        const reqBody = req.body
        const result = await createEffectService(reqBody)
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { effect: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Create effect controller error", 400, error)
    }
}

export const updateEffect = async (req: Request<{id: string}>, res: Response) => {
    try {
        const { id } = req.params
        const reqBody = req.body
        const result = await updateEffectService(id, reqBody)
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { effect: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Update effect controller error", 400, error)
    }
}

export const deleteEffect = async (req: Request<{id: string}>, res: Response) => {
    try {
        const { id } = req.params
        const result = await deleteEffectService(id)
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { effect: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Delete effect controller error", 400, error)
    }
}