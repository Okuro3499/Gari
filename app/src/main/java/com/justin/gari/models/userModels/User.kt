package com.justin.gari.models.userModels

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

    @SerializedName("driver_licence_url")
    @Expose
    val driver_licence_url: String?,

    @SerializedName("national_id_url")
    @Expose
    val national_id_url: String?,

    @SerializedName("user_photo_url")
    @Expose
    val user_photo_url: String?,

    @SerializedName("contact1_name")
    @Expose
    val contact1_name: String?,

    @SerializedName("contact1_relationship")
    @Expose
    val contact1_relationship: String?,

    @SerializedName("contact1_mobile")
    @Expose
    val contact1_mobile: String?,

    @SerializedName("contact2_name")
    @Expose
    val contact2_name: String?,

    @SerializedName("contact2_relationship")
    @Expose
    val contact2_relationship: String?,

    @SerializedName("contact2_mobile")
    @Expose
    val contact2_mobile: String?,
    )