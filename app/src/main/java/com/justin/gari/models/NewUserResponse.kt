package com.justin.gari.models

import com.google.gson.annotations.SerializedName

data class NewUserResponse(
    @SerializedName("newUser") val newUser: User,
)