package com.justin.gari.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cars (
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
    val passengers: Int?,

    @SerializedName("company")
    @Expose
    val company: String?,

    @SerializedName("price")
    @Expose
    val price: Int?,

    @SerializedName("doors")
    @Expose
    val doors: Int?
)