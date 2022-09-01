package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.SerializedName

data class UserPhotoUrl(
    @SerializedName("user_photo_url") val user_photo_url: String?,
)