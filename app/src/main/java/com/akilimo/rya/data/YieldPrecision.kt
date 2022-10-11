package com.akilimo.rya.data

data class YieldPrecision(
    val precisionString: String,
    val precisionType: String = "low",
    val triangleCount: Int = 3,
    val plantCount: Int = 9,
    val imageId: Int = 0
)
