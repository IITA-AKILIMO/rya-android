package com.akilimo.rya.utils


object StringToNumberFactory {
    /**
     * @param numberString
     * @return Int
     */
    @JvmStatic
    fun stringToInt(numberString: String): Int {
        try {
            return numberString.toInt()
        } catch (_: Exception) {
        }
        return 0
    }

    /**
     * @param numberString
     * @return Double
     */
    @JvmStatic
    fun stringToDouble(numberString: String): Double {
        try {
            return numberString.toDouble()
        } catch (_: Exception) {
        }
        return 0.0
    }
}
