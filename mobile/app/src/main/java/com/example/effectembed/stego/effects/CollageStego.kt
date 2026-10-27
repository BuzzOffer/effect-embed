package com.example.effectembed.stego.effects

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CollageStego: StegoEffect {
    override val id = "collage"
    override val name = "Collage"

    override suspend fun embed(coverImage: Bitmap, data: ByteArray): Bitmap {
        return withContext(Dispatchers.Default) {
            val width = coverImage.width
            val height = coverImage.height
            val totalPixels = width * height

            val pixels = IntArray(totalPixels)

            coverImage.getPixels(pixels, 0, width, 0, 0, width, height)

            val colorY = (255 shl 24) or (255 shl 16) or (255 shl 8) or 0
            val colorX = (255 shl 24) or (255 shl 16) or (150 shl 8) or 0
            val colorA = (255 shl 24) or (255 shl 16) or (150 shl 8) or 100
            val colorB = (255 shl 24) or (255 shl 16) or (150 shl 8) or 70

            //cells are subimages
            val cellWidth = width / 5
            val cellHeight = height / 3

            //Bit extraction
            var dataByteIndex = data.size - 1
            var dataBitOffset = 0
            val totalDataBits = data.size * 8
            var bitsEmbedded = 0

            for (i in pixels.indices) {
                //calculate x and y coordinates of current pixel
                val x = i % width
                val y = i / width

                //calculate which subimage this pixel belongs to
                val col = minOf(x / cellWidth, 4) //col number. possible columns: 0, 1, 2, 3, 4
                val row = minOf(y / cellHeight, 2) //row number. possible rows: 0, 1, 2

                //start and end X coords for the current subimage
                val cellStartX = col * cellWidth
                val cellEndX = if (col == 4) width - 1 else (col + 1) * cellWidth - 1
                //start and end Y coords for the current subimage
                val cellStartY = row * cellHeight
                val cellEndY = if (row == 2) height - 1 else (row + 1) * cellHeight - 1

                //check if current pixel is part of the border pixels
                val isVerticalBorder = (x == cellStartX) || (x == cellEndX)
                val isHorizontalBorder = (y == cellStartY) || (y == cellEndY)

                //create borders and embed bits
                if (isHorizontalBorder) {
                    if (bitsEmbedded < totalDataBits) {
                        val currentByte = data[dataByteIndex].toInt() and 0xFF
                        val dataBit = (currentByte shr dataBitOffset) and 1
                        if (dataBit == 0) {
                            pixels[i] = colorA
                        } else {
                            pixels[i] = colorB
                        }
                        bitsEmbedded++
                        dataBitOffset++
                        if (dataBitOffset == 8) {
                            dataBitOffset = 0
                            dataByteIndex--
                        }
                    } else {
                        pixels[i] = colorX
                    }
                } else if (isVerticalBorder) {
                    pixels[i] = colorY
                }
            }
            val resultBitmap = createBitmap(width, height)
            resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            resultBitmap
        }
    }

    override suspend fun extract(stegoImage: Bitmap): ByteArray {
        return withContext(Dispatchers.Default) {
            val width = stegoImage.width
            val height = stegoImage.height
            val totalPixels = width * height

            val pixels = IntArray(totalPixels)

            //cells are subimages
            val cellHeight = height / 3

            stegoImage.getPixels(pixels, 0, width, 0, 0, width, height)
            val extractedBytes = mutableListOf<Byte>()
            var currentByte = 0
            var bitOffset = 0

            for (i in pixels.indices) {
                //calculate y coordinates of current pixel
                val y = i / width

                //calculate which horizontal row of images this pixel belongs to
                val row = minOf(y / cellHeight, 2) //row number. possible rows: 0, 1, 2

                //start and end Y coords for the current subimage
                val cellStartY = row * cellHeight
                val cellEndY = if (row == 2) height - 1 else (row + 1) * cellHeight - 1

                val isHorizontalBorder = (y == cellStartY) || (y == cellEndY)

                if (!isHorizontalBorder) {
                    continue
                }

                //read blue value only
                val valB = pixels[i] and 0xFF
                var extractedBit: Int
                if (valB == 100) {
                    extractedBit = 0
                } else if (valB == 70) {
                    extractedBit = 1
                } else {
                    break
                }

                currentByte = currentByte or (extractedBit shl bitOffset)
                bitOffset++

                if (bitOffset == 8) {
                    extractedBytes.add(0, currentByte.toByte())
                    bitOffset = 0
                    currentByte = 0
                }
            }
            extractedBytes.toByteArray()
        }
    }
}