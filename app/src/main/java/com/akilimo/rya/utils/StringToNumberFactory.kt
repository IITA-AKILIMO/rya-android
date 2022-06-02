package com.akilimo.rya.utils


object StringToNumberFactory {
    /**
     * @param numberString
     * @return Double
     */
    @JvmStatic
    fun stringToDouble(numberString: String): Double {
        try {
            return numberString.toDouble()
        } catch (ex: Exception) {}
        return 0.0
    }

}
