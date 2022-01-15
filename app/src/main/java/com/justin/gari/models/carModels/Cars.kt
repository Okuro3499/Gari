package com.justin.gari.models.carModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cars (
    @SerializedName("car_id")
    @Expose
    val car_id: String?,

    @SerializedName("car_name")
    @Expose
    val car_name: String?,

    @SerializedName("status")
    @Expose
    val status: String?,

    @SerializedName("transmission")
    @Expose
    val transmission: String?,

    @SerializedName("engine")
    @Expose
    val engine: String?,

    @SerializedName("color")
    @Expose
    val color: String?,

    @SerializedName("registration")
    @Expose
    val registration: String?,

    @SerializedName("passengers")
    @Expose
    val passengers: String?,

    @SerializedName("company")
    @Expose
    val company: String?,

    @SerializedName("price")
    @Expose
    val price: String?,

    @SerializedName("doors")
    @Expose
    val doors: String?
)