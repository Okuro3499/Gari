package com.justin.gari.models.bookingCarModels

import com.google.gson.annotations.SerializedName

data class BookingsResponseObject(
    @SerializedName("booking_id") val booking_id: String?,
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("client_id") val client_id: String?,
    @SerializedName("book_date_from") val book_date_from: String?,
    @SerializedName("book_date_to") val book_date_to: String?,
    @SerializedName("destination") val destination: String?,
    @SerializedName("drive") val drive: String?,
    @SerializedName("total_days") val total_days: String?,
    @SerializedName("total_amount") val total_amount: String?,
    @SerializedName("car_name") val car_name: String?,
    @SerializedName("transmission") val transmission: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("front_view") val front_view: String?
)