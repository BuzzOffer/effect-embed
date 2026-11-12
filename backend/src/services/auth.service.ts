import { poolAdmin } from "../config/database.ts";
import * as argon2 from "argon2";
import jwt from "jsonwebtoken";

export const loginUser = async (reqBody: any) => {
    try {
        const { email, password } = reqBody
        const { rows: result } = await poolAdmin.query(`SELECT id, username, email, password, role FROM users WHERE email = $1`, [email])

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
            {id: authUser.id, role: authUser.role},
            jwtSecret, 
            { expiresIn: '7d' }
        )

        return {
            success: true,
            message: 'Successfully logged in user',
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
            message: "Login service error: " + error,
            error
        }
    }
}

export const registerUser = async (reqBody: any) => {
    try {
        const { username, email, password, role } = reqBody;
        const checkEmailResult = await poolAdmin.query(`SELECT id, username, email, role, created_at FROM users WHERE email = $1`, [email]);
        if (checkEmailResult.rowCount !== 0) {
            return {
                success: false,
                message: "Email is already registered",
                error: "Email is already registered"
            }
        }

        const hash = await argon2.hash(password);

        const registerResult = await poolAdmin.query(`INSERT INTO users (username, email, password, role) VALUES ($1, $2, $3, $4) RETURNING id, username, email, role, created_at`,
            [username, email, hash, role]
        )

        const registeredUser = registerResult.rows[0]

        if (registerResult.rowCount === 0) {
            return {
                success: false,
                message: "Error registering user",
                error: "Error registering user"
            }
        }

        return {
            success: true,
            message: "User registered successfully",
            data: registeredUser
        }
    } catch (error) {
        return {
            success: false,
            message: "Register user service error",
            error: "Register user service error"
        }
    }
}