package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.SerializedName

data class SaveCar(
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("saving_datetime") val saving_datetime: String?
)