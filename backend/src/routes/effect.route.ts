import { Router } from "express";
import { createEffect, deleteEffect, getEffects, updateEffect } from "../controllers/effect.controller.ts";
import { authenticateJWT, requireRole } from "../middleware/auth.middleware.ts";

const router = Router()

/**
 * @route GET /api/effects
 * @description Get all effect logs
 * @access Private
 */
router.get('/', authenticateJWT, requireRole('admin'), getEffects);

/**
 * @route POST /api/effects/create
 * @description Create effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.post('/create', authenticateJWT, requireRole('admin'), createEffect);

/**
 * @route PUT /api/effects/:id
 * @description Update effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.put('/:id', authenticateJWT, requireRole('admin'), updateEffect);

/**
 * @route DELETE /api/effects/:id
 * @description Delete effect
 * @access Private
 */
router.delete('/:id', authenticateJWT, requireRole('admin'), deleteEffect);

export default router