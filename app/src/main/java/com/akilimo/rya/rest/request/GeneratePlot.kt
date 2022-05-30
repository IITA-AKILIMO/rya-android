package com.akilimo.rya.rest.request

import com.fasterxml.jackson.annotation.JsonProperty

class GeneratePlot(
    @get:JsonProperty("results")
    val results: List<Double>,
    @get:JsonProperty("field_area")
    val fieldArea: Double,
    @get:JsonProperty("area_unit")
    val areaUnit: String,
    @get:JsonProperty("file_name")
    val fileName: String,
    @get:JsonProperty("ext")
    val ext: String = ".png"
)
