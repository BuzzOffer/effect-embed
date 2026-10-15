package com.example.effectembed.stego.effects

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap

class GlitchStego: StegoEffect {
    override val id = "glitch"
    override val name = "Glitch"

    override suspend fun embed(coverImage: Bitmap, data: ByteArray): Bitmap {
        return withContext(Dispatchers.Default) {
            val threshold = 100
            var extensionLength = 0
            val width = coverImage.width
            val height = coverImage.height
            val totalPixels = width * height

            val pixels = IntArray(totalPixels)
            val modifiedColumns = BooleanArray(width)
            val modifiedPixels = BooleanArray(totalPixels)

            coverImage.getPixels(pixels, 0, width, 0, 0, width, height)

            //Bit extraction
            var dataByteIndex = data.size - 1
            var dataBitOffset = 0
            val totalDataBits = data.size * 8
            var bitsEmbedded = 0

            for (i in pixels.indices) {
                val pixel = pixels[i]
                if (modifiedPixels[i]) {
                    continue
                }

                val alpha = (pixel shr 24) and 0xFF
                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF

                val pAv = (red + green + blue) / 3
                if (pAv >= threshold) {
                    modifiedPixels[i] = true
                    val x = i % width //current column
                    var count = 0
                    if (bitsEmbedded >= totalDataBits) {
                        extensionLength = (1..50).random()
                    } else if (modifiedColumns[x]) {
                        extensionLength = (1..50).random()
                    } else {
                        val currentByte = data[dataByteIndex].toInt() and 0xFF
                        val dataBit = (currentByte shr dataBitOffset) and 1
                        if (dataBit == 0) {
                            extensionLength = 101
                        } else {
                            extensionLength = 130
                        }

                        bitsEmbedded++
                        dataBitOffset++
                        if (dataBitOffset == 8) {
                            dataBitOffset = 0
                            dataByteIndex--
                        }
                    }
                    var verticalIndex = i + width
                    while (count != extensionLength && verticalIndex < totalPixels) {
                        pixels[verticalIndex] = (alpha shl 24) or (red shl 16) or (green shl 8) or blue
                        modifiedPixels[verticalIndex] = true
                        count++
                        verticalIndex += width
                    }
                    if (extensionLength == 101 || extensionLength == 130) {
                        if (count != extensionLength) {
                            verticalIndex -= width
                            pixels[verticalIndex] = (alpha shl 24) or (255 shl 16) or (0 shl 8) or 255 //log pink endpoint if embedding is unfinished and reached limit
                        }
                    }
                    if (verticalIndex < totalPixels) {
                        if (pixels[verticalIndex] == pixels[i]) {
                            pixels[verticalIndex] = (alpha shl 24) or ((0..255).random() shl 16) or ((0..255).random() shl 8) or (0..255).random()
                        }
                    }
                    modifiedColumns[x] = true
                }
            }

            val resultBitmap = createBitmap(width, height)
            resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            resultBitmap
        }
    }

    override suspend fun extract(stegoImage: Bitmap): ByteArray {
        return withContext(Dispatchers.Default) {
            val threshold = 100
            val width = stegoImage.width
            val height = stegoImage.height
            val totalPixels = width * height

            val pixels = IntArray(totalPixels)
            val modifiedColumns = BooleanArray(width)
            val modifiedPixels = BooleanArray(totalPixels)

            stegoImage.getPixels(pixels, 0, width, 0, 0, width, height)
            val extractedBytes = mutableListOf<Byte>()
            var currentByte = 0
            var bitOffset = 0

            for (i in pixels.indices) {
                val pixel = pixels[i]

                if (modifiedPixels[i]) {
                    continue
                }

                val red = (pixel shr 16) and 0xFF
                val green = (pixel shr 8) and 0xFF
                val blue = pixel and 0xFF

                val pAv = (red + green + blue) / 3
                if (pAv >= threshold) {
                    modifiedPixels[i] = true
                    val x = i % width
                    if (!modifiedColumns[x]) {
                        var count = 0
                        var verticalIndex = i + width

                        while(verticalIndex < totalPixels) {
                            val nextPixel = pixels[verticalIndex]

                            if (nextPixel == pixel) {
                                count++
                                modifiedPixels[verticalIndex] = true
                                verticalIndex += width
                            } else {
                                break
                            }
                        }
                        var bitExtracted = -1
                        if (count == 101) {
                            bitExtracted = 0
                        } else if (count == 130) {
                            bitExtracted = 1
                        }
                        if (bitExtracted != -1) {
                            currentByte = currentByte or (bitExtracted shl bitOffset)
                            bitOffset++

                            if (bitOffset == 8) {
                                extractedBytes.add(0, currentByte.toByte())
                                bitOffset = 0
                                currentByte = 0
                            }
                        }
                        modifiedColumns[x] = true
                    }
                }
            }
            extractedBytes.toByteArray()
        }
    }
}