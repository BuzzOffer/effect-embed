import { Router } from "express";
import stegoRoutes from './stego.route.ts'
import effectRoutes from './effect.route.ts'
import authRoutes from './auth.route.ts'

const router = Router()

router.use('/effects', effectRoutes)
router.use('/stego', stegoRoutes)
router.use('/auth', authRoutes)

export default router