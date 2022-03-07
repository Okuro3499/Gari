package com.justin.gari.models.uploadImagesModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageInfoResponse(
    @Expose
    @SerializedName("client_id") val client_id: String?,
    @Expose
    @SerializedName("driver_licence_url") val driver_licence_url: String?,
    @Expose
    @SerializedName("national_id_url") val national_id_url: String?,
    @Expose
    @SerializedName("user_photo_url") val user_photo_url: String?,
    @Expose
    @SerializedName("contact1_name") val contact1_name: String?,
    @Expose
    @SerializedName("contact1_relationship") val contact1_relationship: String?,
    @Expose
    @SerializedName("contact1_mobile") val contact1_mobile: String?,
    @Expose
    @SerializedName("contact2_name") val contact2_name: String?,
    @Expose
    @SerializedName("contact2_relationship") val contact2_relationship: String?,
    @Expose
    @SerializedName("contact2_mobile") val contact2_mobile: String?
)