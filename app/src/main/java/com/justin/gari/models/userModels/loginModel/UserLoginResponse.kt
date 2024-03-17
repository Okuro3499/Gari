package com.justin.gari.models.userModels.loginModel

import com.google.gson.annotations.SerializedName
import com.justin.gari.models.userModels.User

data class UserLoginResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("user") var user: User?,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("error") val error: String?
)