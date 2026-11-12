package com.example.effectembed.stego.domain

import android.util.Log
import com.example.effectembed.core.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class CryptoEngine (
    private val keyManager: KeyManager
) {

    companion object {
        private const val AES_GCM = "AES/GCM/NoPadding"
        private const val PBKDF2 = "PBKDF2WithHmacSHA256"

        private const val HEADER_NO_PASSKEY = 0x00.toByte()
        private const val HEADER_WITH_PASSKEY = 0x01.toByte()

        private const val SALT_LENGTH = 16
        private const val IV_LENGTH = 12
        private const val TAG_LENGTH = 128
        private const val KEY_LENGTH = 256
        private const val ITERATIONS = 10000
    }
    private val DEFAULT_KEY = keyManager.getDefaultKey()

    //Get app key as bytes
    private fun getAppSecretKey(): SecretKeySpec {
        val len = DEFAULT_KEY.length //get length, 64 characters (32 bytes)
        val keyBytes = ByteArray(len / 2) //(32 bytes)
        for (i in 0 until len step 2) { //convert each pair of hex characters into a byte
            keyBytes[i/2] = ((Character.digit(DEFAULT_KEY[i], 16) shl 4) + Character.digit(DEFAULT_KEY[i + 1], 16)).toByte()
        }
        return SecretKeySpec(keyBytes, "AES")
    }

    suspend fun encrypt(message: String, passkey: String?): ByteArray {
        return withContext(Dispatchers.Default) {
            var dataToEncrypt = message.toByteArray(Charsets.UTF_8)

            val hasPasskey = !passkey.isNullOrBlank()
            if (hasPasskey) {
                //generate random salt
                val secureRandom = SecureRandom()
                val salt = ByteArray(SALT_LENGTH).apply { secureRandom.nextBytes(this) }

                //convert passkey to aes key
                val factory = SecretKeyFactory.getInstance(PBKDF2)
                val spec = PBEKeySpec(passkey.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
                val passkeyKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

                //encrypt the data
                val cipher = Cipher.getInstance(AES_GCM)
                cipher.init(Cipher.ENCRYPT_MODE, passkeyKey)

                val iv1 = cipher.iv
                val encryptedBytes = cipher.doFinal(dataToEncrypt)

                //bundle the encrypted data as [salt][iv][ciphertext]
                val passkeyBundle = ByteArray(SALT_LENGTH + IV_LENGTH + encryptedBytes.size)
                System.arraycopy(salt, 0, passkeyBundle, 0, SALT_LENGTH)
                System.arraycopy(iv1, 0, passkeyBundle, SALT_LENGTH, IV_LENGTH)
                System.arraycopy(encryptedBytes, 0, passkeyBundle, SALT_LENGTH + IV_LENGTH, encryptedBytes.size)

                dataToEncrypt = passkeyBundle
            }

            val appCipher = Cipher.getInstance(AES_GCM)
            appCipher.init(Cipher.ENCRYPT_MODE, getAppSecretKey())
            val iv2 = appCipher.iv
            val appEncryptedBytes = appCipher.doFinal(dataToEncrypt)

            //final bundle [header][iv2][appciphertext]
            val finalPayload = ByteArray(1 + IV_LENGTH + appEncryptedBytes.size)
            if (hasPasskey) {
                finalPayload[0] = HEADER_WITH_PASSKEY
            } else {
                finalPayload[0] = HEADER_NO_PASSKEY
            }

            System.arraycopy(iv2, 0, finalPayload, 1, IV_LENGTH)
            System.arraycopy(appEncryptedBytes, 0, finalPayload, 1 + IV_LENGTH, appEncryptedBytes.size)
            Log.d("CryptoEngineEncrypt", "Final Payload: ${finalPayload.contentToString()}")

            finalPayload
        }
    }

    suspend fun decrypt(encryptedMessage: ByteArray, providedPasskey: String?): String {
        return withContext(Dispatchers.Default) {
            try {
                Log.d("CryptoEngine", "Received message: ${encryptedMessage.contentToString()}")
                val header = encryptedMessage[0]
                val iv2 = encryptedMessage.copyOfRange(1, 1 + IV_LENGTH)
                val appCipherText = encryptedMessage.copyOfRange(1 + IV_LENGTH, encryptedMessage.size)

                val appCipher = Cipher.getInstance(AES_GCM)
                appCipher.init(Cipher.DECRYPT_MODE, getAppSecretKey(), GCMParameterSpec(TAG_LENGTH, iv2))
                val appDecryptedBytes = appCipher.doFinal(appCipherText)

                when (header) {
                    HEADER_NO_PASSKEY -> {
                        String(appDecryptedBytes, Charsets.UTF_8)
                    }
                    HEADER_WITH_PASSKEY -> {
                        if (providedPasskey.isNullOrBlank()) {
                            ""
                        } else {
                            val salt = appDecryptedBytes.copyOfRange(0, SALT_LENGTH)
                            val iv1 = appDecryptedBytes.copyOfRange(SALT_LENGTH, SALT_LENGTH + IV_LENGTH)
                            val cipherText = appDecryptedBytes.copyOfRange(SALT_LENGTH + IV_LENGTH, appDecryptedBytes.size)

                            val factory = SecretKeyFactory.getInstance(PBKDF2)
                            val spec = PBEKeySpec(providedPasskey.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
                            val passkeyKey = SecretKeySpec(factory.generateSecret(spec).encoded, "AES")

                            val cipher = Cipher.getInstance(AES_GCM)
                            cipher.init(Cipher.DECRYPT_MODE, passkeyKey, GCMParameterSpec(TAG_LENGTH, iv1))
                            val plainTextBytes = cipher.doFinal(cipherText)

                            String(plainTextBytes, Charsets.UTF_8)
                        }
                    }
                    else -> "Invalid or corrupted header template"
                }

            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }


}