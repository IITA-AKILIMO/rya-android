package com.akilimo.rya.utils

import android.content.Context
import com.akilimo.rya.R
import com.akilimo.rya.entities.CurrencyEntity
import com.akilimo.rya.repos.CurrencyDao
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader

class PrefillCurrency(private val currencyDao: CurrencyDao) {

    private fun loadJSONArray(context: Context): JSONArray {

        val inputStream = context.resources.openRawResource(R.raw.currencies)

        BufferedReader(inputStream.reader()).use {
            return JSONArray(it.readText())
        }
    }

    fun fillWithStartingNotes(context: Context) {
        try {
            val notes = loadJSONArray(context)
            for (i in 0 until notes.length()) {
                val item = notes.getJSONObject(i)
                val countryName = item.getString("COUNTRY")
                val countryCode = item.getString("COUNTRY_CODE")
                val currencyCode = item.getString("CURRENCY_CODE")
                val currencyName = item.getString("NAME_OF_CURRENCY")

                val noteEntity = CurrencyEntity(
                    countryName = countryName,
                    countryCode = countryCode,
                    currencyCode = currencyCode,
                    currencyName = currencyName
                )

                currencyDao.insert(noteEntity)
            }
        } catch (e: JSONException) {
//            Timber.d("fillWithStartingNotes: $e")
            val k = e
        }
    }
}
