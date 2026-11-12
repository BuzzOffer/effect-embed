import type { Response } from "express"

export const successResponse = (res: Response, data: any = null, message: String = "Success", statusCode: number = 200) => {
    return res.status(statusCode).json({
        success: true,
        message,
        data
    })
}

export const errorResponse = (res: Response, message: String = "Error", statusCode: number = 400, errors: any = null) => {
    return res.status(statusCode).json({
        success: false,
        message: message + ": " + errors,
        errors
    })
}

export const unauthorizedResponse = (res: Response, message: String = "Unauthorized") => {
    return errorResponse(res, message, 401)
}