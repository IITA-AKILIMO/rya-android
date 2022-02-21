package com.akilimo.rya.data

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty


@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    fieldVisibility = JsonAutoDetect.Visibility.ANY
)
data class RyaPlot(
    @get:JsonProperty("file_name")
    val fileName: String = "device_20",
    @get:JsonProperty("ext")
    val fileExtension: String = ".png",
)
