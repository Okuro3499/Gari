package com.justin.gari.models

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("error") val error: String?
)
