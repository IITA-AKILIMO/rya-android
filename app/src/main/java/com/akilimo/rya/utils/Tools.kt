package com.akilimo.rya.utils

import android.content.*
import android.graphics.Bitmap
import android.graphics.Matrix
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.json.JSONObject
import java.util.*


object Tools {
    private val LOG_TAG = Tools::class.java.simpleName
    private val mapper = ObjectMapper()

    /**
     * @param objectClass
     * @return JsonObject
     */
    @JvmStatic
    fun prepareJsonObject(objectClass: Any): JSONObject? {
        var jsonObject: JSONObject? = null
        val jsonString: String
        try {
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
            jsonString = mapper.writeValueAsString(objectClass)
            jsonObject = JSONObject(jsonString)
        } catch (ex: Exception) {

        }
        return jsonObject
    }

    @JvmStatic
    fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
}
