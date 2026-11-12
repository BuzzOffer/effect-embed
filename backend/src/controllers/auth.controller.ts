import type { Request, Response } from "express";
import { errorResponse, successResponse } from "../utils/response.ts";
import { loginUser, registerUser } from "../services/auth.service.ts";

export const login = async (req: Request, res: Response) => {
    try {
        const reqBody = req.body;

        const result = await loginUser(reqBody);

        if (!result.success) {
            return errorResponse(res, result.message, 401, result.error);
        }

        return successResponse(res, result.data, result.message);
    } catch (error) {
        return errorResponse(res, "Login controller error", 500, error);
    }
}

export const register = async (req: Request, res: Response) => {
    try {
        const reqBody = req.body;

        const result = await registerUser(reqBody);

        if (!result.success) {
            return errorResponse(res, result.message, 401, result.error);
        }

        return successResponse(res, result.data, result.message);
    } catch (error) {

    }
}