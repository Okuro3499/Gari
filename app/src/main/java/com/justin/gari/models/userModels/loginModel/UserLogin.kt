package com.justin.gari.models.userModels.loginModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLogin(
    @Expose
    @SerializedName("email") val email: String?,

    @Expose
    @SerializedName("password") val Password: String?
)