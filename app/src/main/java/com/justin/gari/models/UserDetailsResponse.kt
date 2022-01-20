package com.justin.gari.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDetailsResponse(
    @Expose
    @SerializedName("single_client") val single_client: User,
)