package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import android.util.Log

class LocationRepository {
    suspend fun getAreaName(lat: Double, lon: Double): String {
        return try {
            val response = RetrofitInstance.nominatimApi.reverseGeocode(lat, lon)
            val address = response.address
            Log.d("LocationRepository", "Nominatim raw address: $address | displayName: ${response.displayName}")

            // Try progressively broader area fields — Nominatim field presence varies by country.
            // Malaysian responses often return village/county rather than suburb/city/town.
            val area = address?.suburb
                ?: address?.quarter
                ?: address?.village
                ?: address?.municipality
                ?: address?.cityDistrict
                ?: address?.city
                ?: address?.town
                ?: address?.county
                // Last resort: grab the first meaningful segment of the full display name
                ?: response.displayName.split(", ").firstOrNull { it.isNotBlank() }
                ?: "Unknown area"

            if (address?.state != null) "$area, ${address.state}" else area
        } catch (e: Exception) {
            Log.e("LocationRepository", "getAreaName failed for ($lat, $lon)", e)
            "Unknown area"
        }
    }
}
