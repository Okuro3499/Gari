package com.justin.gari.models.bookingCarModels

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookCarResponseObject(
    @SerializedName("booking_id") val booking_id: String?,
    @SerializedName("car_id") val car_id: String?,
    @SerializedName("client_id") val client_id: String?,
    @SerializedName("book_date_from") val book_date_from: Date?,
    @SerializedName("book_date_to") val book_date_to: Date?,
    @SerializedName("destination") val destination: String?,
    @SerializedName("drive") val drive: String?,
    @SerializedName("total_days") val total_days: String?,
    @SerializedName("total_amount") val total_amount: String?
)