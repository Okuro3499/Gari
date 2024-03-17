package com.justin.gari.models.userModels.loginModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserLogin(
    @Expose
    @SerializedName("phone_number") val phone_number: String?,
    @Expose
    @SerializedName("password") val password: String?
)