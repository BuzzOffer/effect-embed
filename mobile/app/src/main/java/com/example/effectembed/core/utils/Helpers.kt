package com.example.effectembed.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun saveToGallery(context: Context, bitmap: Bitmap): Boolean {
    return withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "modified_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = resolver.insert(imageCollection, contentValues)

        try {
            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    val isSaved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                    return@withContext isSaved
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            if (uri != null) {
                resolver.delete(uri, null, null)
            }
            false
        }
    }
}