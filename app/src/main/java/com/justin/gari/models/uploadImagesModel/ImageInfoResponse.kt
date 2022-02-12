package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageInfoResponse(
    @Expose
    @SerializedName("imageInfo") val imageInfo: ImageInfoResponseObject
)