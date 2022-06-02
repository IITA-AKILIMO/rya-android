package com.akilimo.rya.rest.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class YieldEstimate(
    @JsonProperty("result")
    val result: List<Double>
)
