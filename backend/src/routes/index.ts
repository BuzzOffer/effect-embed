import { Router } from "express";
import stegoRoutes from './stego.route.ts'
import effectRoutes from './effect.route.ts'

const router = Router()

router.use('/effects', effectRoutes)
router.use('/stego', stegoRoutes)

export default router