package com.akilimo.rya.data

import com.fasterxml.jackson.annotation.JsonProperty


data class RyaEstimate(
    @JsonProperty("field_area")
    val fieldArea: Double,

    @JsonProperty("precision_type")
    val precisionType: String,

    @JsonProperty("plant_counts")
    val plantCounts: List<Int>,

    @JsonProperty("plant_RMs")
    val plantRMs: List<Double>,
)
