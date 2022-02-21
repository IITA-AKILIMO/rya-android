package com.akilimo.rya.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 	{
"country": null,
"currencyName": "US Dollar",
"currencyCode": "USD",
"currencySymbol": "$",
"currencyNativeSymbol": "$",
"namePlural": "US dollars"
}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class Currency(
    @JsonProperty("country")
    val country: String?,
    @JsonProperty("currencyName")
    val currencyName: String,
    @JsonProperty("currencyCode")
    val currencyCode: String,
    @JsonProperty("currencySymbol")
    val currencySymbol: String,
    @JsonProperty("currencyNativeSymbol")
    val currencyNativeSymbol: String,
//    @JsonProperty("namePlural")
//    val namePlural: String,
)
