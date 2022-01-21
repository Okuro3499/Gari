package com.justin.gari.models.userModels.signUpModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NewUserData(
    @SerializedName("first_name")
    @Expose
    val first_name: String?,

    @SerializedName("last_name")
    @Expose
    val last_name: String?,

    @SerializedName("email")
    @Expose
    val email: String?,

    @SerializedName("mobile")
    @Expose
    val mobile: String?,

    @SerializedName("county")
    @Expose
    val county: String?,

    @SerializedName("district")
    @Expose
    val district: String?,

    @SerializedName("estate")
    @Expose
    val estate: String?,

    @SerializedName("landmark")
    @Expose
    val landmark: String?,

    @SerializedName("password")
    @Expose
    val password: String?
)