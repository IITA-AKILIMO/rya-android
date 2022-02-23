package com.akilimo.rya.data

import com.akilimo.rya.enums.PrecisionTypes
import com.fasterxml.jackson.annotation.JsonProperty


data class RyaEstimate(
    @get:JsonProperty("field_area")
    val fieldArea: Double,

    @get:JsonProperty("precision_type")
    val precisionType: PrecisionTypes,

    @get:JsonProperty("plant_counts")
    val plantCounts: List<Int>,

    @get:JsonProperty("plant_RMs")
    val plantRms: List<Double>,
)
