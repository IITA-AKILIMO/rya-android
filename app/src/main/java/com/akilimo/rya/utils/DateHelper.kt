package com.akilimo.rya.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    var format = "dd/MM/yyyy"

    private var simpleDateFormat: SimpleDateFormat? = null

    private val simpleDateFormatter: SimpleDateFormat
        get() = SimpleDateFormat(format, Locale.UK)

    fun getMinDate(minMonths: Int): Calendar {
        return getFutureOrPastMonth(minMonths)
    }

    private fun getFutureOrPastMonth(maxMonth: Int): Calendar {
        simpleDateFormat = simpleDateFormatter
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, maxMonth)
        return cal
    }
}