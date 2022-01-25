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
    val doors: String?,

    @SerializedName("drive")
    @Expose
    val drive: String?,

    @SerializedName("front_view")
    @Expose
    val front_view: String?,

    @SerializedName("back_view")
    @Expose
    val back_view: String?,

    @SerializedName("right_view")
    @Expose
    val right_view: String?,

    @SerializedName("left_view")
    @Expose
    val left_view: String?,

    @SerializedName("interior_1")
    @Expose
    val interior_1: String?,

    @SerializedName("interior_2")
    @Expose
    val interior_2: String?,

    @SerializedName("feature_1")
    @Expose
    val feature_1: String?,

    @SerializedName("feature_2")
    @Expose
    val feature_2: String?,

    @SerializedName("feature_3")
    @Expose
    val feature_3: String?,

    @SerializedName("feature_4")
    @Expose
    val feature_4: String?,

    @SerializedName("feature_5")
    @Expose
    val feature_5: String?
)