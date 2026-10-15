package com.example.effectembed.stego.domain

import android.R
import android.graphics.Bitmap
import android.util.Log
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.stego.effects.StegoEffect

class StegoEngine {
    private val cryptoEngine = CryptoEngine(KeyManager())
    var stegoEffect: StegoEffect

    constructor(stegoEffect: StegoEffect) {
        this.stegoEffect = stegoEffect
    }

    fun setEffect(effect: StegoEffect) {
        stegoEffect = effect
    }

    suspend fun embedData(coverImage: Bitmap?, data: String, passkey: String): Bitmap? {
        if (coverImage == null) {
            return null
        }
        val workingBitmap = coverImage.copy(Bitmap.Config.ARGB_8888, true)
        val encryptedData = cryptoEngine.encrypt(data, passkey)
        val resultBitmap = stegoEffect.embed(workingBitmap, encryptedData)
        return resultBitmap
    }

    suspend fun extractData(stegoImage: Bitmap?, passkey: String): String? {
        if (stegoImage == null) {
            return null
        }
        val workingBitmap = stegoImage.copy(Bitmap.Config.ARGB_8888, true)
        val rawMessage = stegoEffect.extract(workingBitmap)
        Log.d("StegoEngine", "Extracted raw message: ${rawMessage.contentToString()}")
        val decryptedMessage = cryptoEngine.decrypt(rawMessage, passkey)
        return decryptedMessage
    }
}