package ru.sorokin.gb_material.model.earth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EarthAPI {

    @GET("planetary/earth/assets")
    fun getEarthPhoto(
        @Query("lon") lon: Float,
        @Query("lat") lat: Float,
        @Query("dim") dim: Float,
        @Query("api_key") apiKey: String,
        @Query("date") dateString: String
    ): Call<EarthResponseData>
}