package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.SerializedName

data class DlUrl(
    @SerializedName("driver_licence_url") val driver_licence_url: String?,
)