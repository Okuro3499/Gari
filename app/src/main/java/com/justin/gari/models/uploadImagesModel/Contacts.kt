package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("client_id")
    @Expose
    val client_id: String?,
    @SerializedName("contact1_name")
    @Expose
    val contact1_name: String?,
    @SerializedName("contact1_relationship")
    @Expose
    val contact1_relationship: String?,
    @SerializedName("contact1_mobile")
    @Expose
    val contact1_mobile: String?,
    @SerializedName("contact2_name")
    @Expose
    val contact2_name: String?,
    @SerializedName("contact2_relationship")
    @Expose
    val contact2_relationship: String?,
    @SerializedName("contact2_mobile")
    @Expose
    val contact2_mobile: String?,
    @SerializedName("driver_licence_url")
    @Expose
    val driver_licence_url: String?,
    @SerializedName("national_id_url")
    @Expose
    val national_id_url: String?,
    @SerializedName("user_photo_url")
    @Expose
    val user_photo_url: String?,
)