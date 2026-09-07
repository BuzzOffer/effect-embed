import pool from "../config/database.ts";
import * as argon2 from "argon2";
import jwt from "jsonwebtoken";

export const loginUser = async (email: string, password: string) => {
    try {
        const { rows: result } = await pool.query(`SELECT id, username, email, password, role FROM users WHERE email = $1`, [email])

        if (result.length === 0) {
            return {
                success: false,
                message: "Invalid Credentials",
                error: "Invalid Credentials"
            }
        }

        const authUser = result[0];

        const isPasswordValid = await argon2.verify(authUser.password, password);
        if (!isPasswordValid) {
            return {
                success: false,
                message: "Invalid Credentials",
                error: "Invalid Credentials"
            }
        }

        const jwtSecret = process.env.JWT_SECRET;
        if (!jwtSecret) {
            return {
                success: false,
                message: "JWT config missing",
                error: "JWT config missing"
            }
        }

        const token = jwt.sign(
            {id: authUser.id, email: authUser.email, role: authUser.role},
            jwtSecret, 
            { expiresIn: '7d' }
        )

        return {
            success: true,
            message: 'Login service successful',
            data: {
                user: {
                    id: authUser.id,
                    username: authUser.username,
                    email: authUser.email,
                    role: authUser.role
                },
                token,
            }
        }

    } catch (error) {
        return {
            success: false,
            message: "Login service error",
            error
        }
    }
}