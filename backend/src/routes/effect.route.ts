import { Router } from "express";
import { createEffect, deleteEffect, getEffects, updateEffect } from "../controllers/effect.controller.ts";

const router = Router()

/**
 * @route GET /api/effects
 * @description Get all effect logs
 * @access Private
 */
router.get('/', getEffects);

/**
 * @route POST /api/effects/create
 * @description Create effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.post('/create', createEffect);

/**
 * @route PUT /api/effects/:id
 * @description Create effect
 * @access Private
 */
// {
//     "name": "name"
// }
router.put('/:id', updateEffect);

/**
 * @route DELETE /api/effects/:id
 * @description Create effect
 * @access Private
 */
router.delete('/:id', deleteEffect);

export default router