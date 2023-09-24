package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.SerializedName

data class SaveCarObject(
    @SerializedName("saved_id") val saved_id: String?,
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("saving_datetime") val saving_datetime: String?,
    @SerializedName("car_name") val car_name: String?,
    @SerializedName("front_view") val front_view: String?,
    @SerializedName("transmission") val transmission: String?,
    @SerializedName("drive") val drive: String?,
    @SerializedName("price") val price: String?
)