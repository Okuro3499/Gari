package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.SerializedName

data class SaveCarObject(
    @SerializedName("saved_id") val saved_id: String?,
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("client_id") val client_id: String?
)