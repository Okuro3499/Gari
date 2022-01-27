package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.SerializedName

data class SaveCarObject(
    @SerializedName("saved_id") val saved_id: String?,
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("client_id") val client_id: String?,
    @SerializedName("car_name") val car_name: String?,
    @SerializedName("drive") val drive: String?,
    @SerializedName("transmission") val transmission: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("front_view") val front_view: String?
)