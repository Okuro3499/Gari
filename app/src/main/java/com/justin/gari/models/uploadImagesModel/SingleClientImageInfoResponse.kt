package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SingleClientImageInfoResponse(
    @Expose
    @SerializedName("single_clientInfo")
    var single_clientInfo: ImageInfoResponse
)