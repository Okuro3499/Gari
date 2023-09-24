package com.justin.gari.models.userModels.loginModel

import com.google.gson.annotations.SerializedName
import com.justin.gari.models.userModels.User

data class UserLoginResponse(
    @SerializedName("users") var users: User,
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
)