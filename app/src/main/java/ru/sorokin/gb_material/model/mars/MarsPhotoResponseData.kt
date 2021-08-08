package ru.sorokin.gb_material.model.mars

import com.google.gson.annotations.SerializedName

data class MarsPhotoResponseData(
    @field:SerializedName("img_src") val url: String?,
    @field:SerializedName("earth_date") val earth_date: String?
)