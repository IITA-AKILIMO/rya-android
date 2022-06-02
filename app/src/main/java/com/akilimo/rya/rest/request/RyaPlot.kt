package com.akilimo.rya.rest.request

import com.fasterxml.jackson.annotation.JsonProperty


data class RyaPlot(
    @get:JsonProperty("file_name")
    val fileName: String = "device_20",
    @get:JsonProperty("ext")
    val fileExtension: String = ".png",
)
