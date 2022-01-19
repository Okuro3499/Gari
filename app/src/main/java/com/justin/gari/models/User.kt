package com.justin.gari.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("client_id")
    @Expose
    val client_id: String?,

    @SerializedName("first_name")
    @Expose
    val first_name: String?,

    @SerializedName("last_name")
    @Expose
    val last_name: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("mobile")
    val mobile: String?,

    @SerializedName("county")
    val county: String?,

    @SerializedName("district")
    val district: String?,

    @SerializedName("estate")
    val estate: String?,

    @SerializedName("landmark")
    val landmark: String?
)