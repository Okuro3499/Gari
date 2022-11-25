package com.justin.gari.models.roleModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RoleDetails(
    @Expose
    @SerializedName("role_id") val roleId: Int?,

    @Expose
    @SerializedName("role_name") val roleName: String?,

    @Expose
    @SerializedName("role_description") val roleDescription: String,

    @Expose
    @SerializedName("created_by") val createdBy: String?,

    @Expose
    @SerializedName("created_on") val createdOn: String?,

    @Expose
    @SerializedName("modified_by") val modifiedBy: String?,

    @Expose
    @SerializedName("last_modified_on") val last_modified_on: String?,
)