import { Router } from "express";
import { createEffect, deleteEffect, getEffects, updateEffect } from "../controllers/effect.controller.ts";
import { authenticateJWT } from "../middleware/auth.middleware.ts";

const router = Router()

/**
 * @route GET /api/effects
 * @description Get all effect logs
 * @access Private
 */
router.get('/', authenticateJWT, getEffects);

/**
 * @route POST /api/effects/create
 * @description Create effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.post('/create', authenticateJWT, createEffect);

/**
 * @route PUT /api/effects/:id
 * @description Create effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.put('/:id', authenticateJWT, updateEffect);

/**
 * @route DELETE /api/effects/:id
 * @description Create effect
 * @access Private
 */
router.delete('/:id', authenticateJWT, deleteEffect);

export default router