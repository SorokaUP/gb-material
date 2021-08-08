package ru.sorokin.gb_material.model.mars

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsAPI {

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPhotos(
        @Query("camera") camera: String,
        @Query("api_key") apiKey: String,
        @Query("earth_date") dateString: String
    ): Call<MarsPhotosResponseData>
}