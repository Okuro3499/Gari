package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.SerializedName

data class DlCloudinaryResponse(
    @SerializedName("driverLicenceCloudinary") val driverLicenceCloudinary: String?
)