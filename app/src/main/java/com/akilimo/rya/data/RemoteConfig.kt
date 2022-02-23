package com.akilimo.rya.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class RemoteConfig(
    @JsonProperty("appName")
    val appName: String,
    @JsonProperty("configName")
    val configName: String,
    @JsonProperty("configValue")
    val configValue: String
)
