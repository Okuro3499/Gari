package com.justin.gari.models

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val Password: String?
)