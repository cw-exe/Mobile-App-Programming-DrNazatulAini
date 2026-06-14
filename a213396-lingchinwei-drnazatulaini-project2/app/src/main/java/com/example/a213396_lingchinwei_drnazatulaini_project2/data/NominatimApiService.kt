package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NominatimApiService {
    @Headers("User-Agent: AtlasLearn-AndroidApp")
    @GET("reverse?format=json")
    suspend fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): NominatimReverseResponse
}
