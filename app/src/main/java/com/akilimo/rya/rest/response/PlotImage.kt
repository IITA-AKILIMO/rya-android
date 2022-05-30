package com.akilimo.rya.rest.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlotImage(
    @JsonProperty("file_name_a")
    val fileNameLean: String,
    @JsonProperty("file_name_b")
    val fileNameFull: String
)
