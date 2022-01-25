package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.SerializedName

data class SaveCar(
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("client_id") val client_id: String?
)