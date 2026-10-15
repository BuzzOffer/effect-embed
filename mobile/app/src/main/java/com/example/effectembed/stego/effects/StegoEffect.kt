package com.example.effectembed.stego.effects

import android.graphics.Bitmap

sealed interface StegoEffect {
    val id: String
    val name: String

    suspend fun embed(coverImage: Bitmap, data: ByteArray): Bitmap
    suspend fun extract(stegoImage: Bitmap): ByteArray
}