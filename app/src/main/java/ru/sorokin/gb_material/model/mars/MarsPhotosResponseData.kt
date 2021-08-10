package ru.sorokin.gb_material.model.mars

import com.google.gson.annotations.SerializedName

data class MarsPhotosResponseData(
    @field:SerializedName("photos") val photos: Array<MarsPhotoResponseData>,
)