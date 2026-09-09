import type { Request, Response, NextFunction } from "express";
import { errorResponse, unauthorizedResponse } from "../utils/response.ts";
import jwt from 'jsonwebtoken';
import type { SignedUserInfo } from "../types/auth.ts";

//extend the express interface Request to include the "user" property
declare global {
    namespace Express {
        interface Request {
            user?: any
        }
    }
}

export const authenticateJWT = (req: Request, res: Response, next: NextFunction) => {
    //extract token from auth header
    const authHeader = req.headers.authorization;
    const token = authHeader ? authHeader.split(' ')[1] : null;

    if (!token) {
        return unauthorizedResponse(res, "Authentication required");
    }

    try {
        const decodedInfo = jwt.verify(token, process.env.JWT_SECRET as string) as SignedUserInfo
        //attach the user to the request object
        req.user = decodedInfo;
        next()
    } catch (error) {
        return errorResponse(res, "Error during authentication", 403)
    }
}

export const requireRole = (...allowedRoles: string[]) => {
    return (req: Request, res: Response, next: NextFunction) => {
        const userInfo = req.user;

        if (!userInfo) {
            return unauthorizedResponse(res, "Authentication required");
        }

        if (!allowedRoles.includes(userInfo.role)) {
            return unauthorizedResponse(res, "This role does not have permission");
        }

        next();
    }
}