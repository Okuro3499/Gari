package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.SerializedName

data class UserPhotoCloudinaryResponse(
    @SerializedName("userPhotoCloudinary") val userPhotoCloudinary: String?
)