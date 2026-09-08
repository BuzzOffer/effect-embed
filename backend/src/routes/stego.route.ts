import { Router } from "express";
import { createStegoLog, deleteStegoLog, getStegoLogById, getStegoLogs } from "../controllers/stego.controller.ts";
import { authenticateJWT } from "../middleware/auth.middleware.ts";

const router = Router()

/**
 * @route GET /api/stego
 * @description Get all stego logs
 * @access Private
 */
router.get('/', authenticateJWT, getStegoLogs);

/**
 * @route GET /api/stego/:id
 * @description Get stego log by id
 * @access Private
 */
router.get('/:id', authenticateJWT, getStegoLogById);

/**
 * @route POST /api/stego/create
 * @description Create stego log
 * @access Private
 */
// {
//     "user_id": "user_id",
//     "effect_name": "effect_name",
//     "type": "embed | extract",
//     "time_taken": "time_taken"
// }
router.post('/create', authenticateJWT, createStegoLog);

/**
 * @route DELETE /api/stego/:id
 * @description Delete stego log
 * @access Private
 */
router.delete('/:id', authenticateJWT, deleteStegoLog);

export default router