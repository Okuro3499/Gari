package com.justin.gari.models.roleModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetRolesResponse(
    @Expose
    @SerializedName("role_details")
    var role_details: RoleDetails
)