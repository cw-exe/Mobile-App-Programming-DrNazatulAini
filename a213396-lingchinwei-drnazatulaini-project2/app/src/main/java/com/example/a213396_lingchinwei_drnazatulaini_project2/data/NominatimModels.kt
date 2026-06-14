package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NominatimReverseResponse(
    @SerialName("display_name") val displayName: String = "",
    val address: NominatimAddress? = null
)

@Serializable
data class NominatimAddress(
    // Primary residential area fields (what Nominatim returns varies by country/zoom level)
    val suburb: String? = null,
    val quarter: String? = null,
    val village: String? = null,
    val municipality: String? = null,
    val city: String? = null,
    val town: String? = null,
    val county: String? = null,          // common in Malaysian responses ("Petaling", etc.)
    @SerialName("city_district") val cityDistrict: String? = null,
    val state: String? = null
)
