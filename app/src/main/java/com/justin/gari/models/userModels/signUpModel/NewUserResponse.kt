package com.justin.gari.models.userModels.signUpModel

import com.google.gson.annotations.SerializedName
import com.justin.gari.models.userModels.User

data class NewUserResponse(
    @SerializedName("newUser") val newUser: User,
)