package com.akilimo.rya.rest.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeneratePlotResp(
    @JsonProperty("plot_images")
    val plotImages: List<PlotImage>
)
