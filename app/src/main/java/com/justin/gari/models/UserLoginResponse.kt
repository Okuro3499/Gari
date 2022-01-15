package com.justin.gari.models

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?
)