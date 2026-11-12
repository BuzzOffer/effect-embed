package com.example.effectembed.core.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.example.effectembed.auth.model.UserInfo
import com.google.gson.Gson
import com.google.gson.JsonElement
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

fun extractUserInfo(data: Any?): UserInfo? {
    Log.d("EXTRACT FUNC", "Success in function")
    try {
        Log.d("EXTRACT FUNC", "Success in try-block")
        //cast dynamic data to a JsonElement
        val jsonElement = data as? JsonElement ?: return null
        val jsonObject = jsonElement.asJsonObject

        //look for the user key
        val userJson = jsonObject.getAsJsonObject("user") ?: return null
        val token = jsonObject.get("token") ?: return null

        userJson.add("token", token)

//        val combinedMap = userJson.toMutableMap().apply {
//            put("token", token)
//        }
//        val combinedJsonObject = JsonObject(combinedMap)
//        Log.d("COMBINED MAP", combinedMap.toString())
//        Log.d("COMBINED JSON OBJ", combinedJsonObject.toString())
//        Log.d("JSON DECODED", Json.decodeFromJsonElement(combinedJsonObject))
        return Gson().fromJson(userJson, UserInfo::class.java)
    } catch (e: Exception) {
        Log.d("EXTRACT FUNC", "Success in catch-block")
        Log.e("EXTRACT FAIL", e.toString())
        e.printStackTrace()
        return null
    }
}