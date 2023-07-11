package com.akilimo.rya.utils


object StringToNumberFactory {
    /**
     * @param numberString
     * @return Int
     */
    @JvmStatic
    fun stringToInt(numberString: String): Int {
        return try {
            numberString.toInt()
        } catch (_: Exception) {
            0
        }
    }

    /**
     * @param numberString
     * @return Double
     */
    @JvmStatic
    fun stringToDouble(numberString: String): Double {
        return try {
            numberString.toDouble()
        } catch (_: Exception) {
            0.0
        }
    }
}
