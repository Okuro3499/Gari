package com.justin.gari.models.userModels.signUpModel

import com.google.gson.annotations.SerializedName

data class NewUserData(
    @SerializedName("role_id") val role_id: Int?,
    @SerializedName( "first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName( "email") val email: String?,
    @SerializedName("phone_number") val phone_number: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("created_by") val created_by: Int?
)