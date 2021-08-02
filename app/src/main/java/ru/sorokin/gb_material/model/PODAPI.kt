package ru.sorokin.gb_material.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PODAPI {

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String, @Query("date") date: String): Call<PODServerResponseData>
}