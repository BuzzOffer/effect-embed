import { Router } from "express";
import { login, register } from "../controllers/auth.controller.ts";

const router = Router()

/**
 * @route POST /api/auth/login
 * @description Login user
 * @access Public
 */
// {
//     "email": "email",
//     "password": "password"
// }
router.post('/login', login);

/**
 * @route POST /api/auth/login
 * @description Login user
 * @access Public
 */
// {
//     "username": "username",
//     "email": "email",
//     "password": "password",
//     "role": "admin | user"
// }
router.post('/register', register);

export default router