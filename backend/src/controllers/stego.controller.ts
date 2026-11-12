import type { Request, Response } from "express";
import { createStegoLogService, deleteStegoLogService, getStegoLogByIdService, getStegoLogsService } from "../services/stego.service.ts";
import { errorResponse, successResponse } from "../utils/response.ts";
import { queryWithRls } from "../utils/db.ts";

export const getStegoLogs = async (req: Request, res: Response) => {
    try {
        const result = await queryWithRls(req.user, getStegoLogsService)
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { stego_log: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Get Stego logs controller error" + error, 400, error)
    }
}

export const getStegoLogById = async (req: Request<{id: string}>, res: Response) => {
    try {
        const { id } = req.params

        if (!id) {
            return errorResponse(res, "No Stego Log ID provided", 400, "Stego Log ID is required")
        }

        const result = await queryWithRls(req.user, getStegoLogByIdService, id)
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { stego_log: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Get Stego logs controller error", 400, error)
    }
}

export const createStegoLog = async (req: Request, res: Response) => {
    try {
        const reqBody = req.body

        const result = await queryWithRls(req.user, createStegoLogService, reqBody)
        
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { stego_log: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Create Stego log controller error", 400, error)
    }
}

export const deleteStegoLog = async (req: Request<{id: string}>, res: Response) => {
    try {
        const { id } = req.params

        const result = await queryWithRls(req.user, deleteStegoLogService, id)
        
        if (!result.success) {
            return errorResponse(res, result.message, 400, result.error)
        }

        return successResponse(res, { stego_log: result.data }, result.message)
    } catch (error) {
        return errorResponse(res, "Delete Stego log controller error", 400, error)
    }
}