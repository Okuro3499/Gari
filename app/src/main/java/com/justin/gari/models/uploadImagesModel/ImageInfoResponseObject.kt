package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.SerializedName

data class ImageInfoResponseObject(
    @SerializedName("client_id") val client_id: String?,
    @SerializedName("driver_licence_url") val driver_licence_url: String?,
    @SerializedName("national_id_url") val national_id_url: String?,
    @SerializedName("user_photo_url") val user_photo_url: String?,
    @SerializedName("contact1_name") val contact1_name: String?,
    @SerializedName("contact1_relationship") val contact1_relationship: String?,
    @SerializedName("contact1_mobile") val contact1_mobile: String?,
    @SerializedName("contact2_name") val contact2_name: String?,
    @SerializedName("contact2_relationship") val contact2_relationship: String?,
    @SerializedName("contact2_mobile") val contact2_mobile: String?
)