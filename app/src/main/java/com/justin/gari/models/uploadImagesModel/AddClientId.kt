package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddClientId(
    @Expose
    @SerializedName("client_id") val client_id: String?,
)