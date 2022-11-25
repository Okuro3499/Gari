package com.justin.gari.models.userModels.signUpModel

import com.google.gson.annotations.SerializedName

data class NewUserData(
    @SerializedName("role_id") val role_id: Int?,
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("mobile") val mobile: Int?,
    @SerializedName("county") val county: String?,
    @SerializedName("district") val district: String?,
    @SerializedName("estate") val estate: String?,
    @SerializedName("landmark") val landmark: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("role_name") val role_name: String?,
    @SerializedName("role_description") val role_description: String?,
    @SerializedName("created_by") val created_by: String?,
    @SerializedName("created_on") val created_on: String?,
)