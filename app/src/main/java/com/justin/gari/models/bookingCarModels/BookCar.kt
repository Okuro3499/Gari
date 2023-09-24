package com.justin.gari.models.bookingCarModels

import com.google.gson.annotations.SerializedName

data class BookCar(
    @SerializedName("car_id") val car_id: Int?,
    @SerializedName("user_id") val user_id: Int?,
    @SerializedName("book_date_from") val book_date_from: String?,
    @SerializedName("book_date_to") val book_date_to: String?,
    @SerializedName("destination") val destination: String?,
    @SerializedName("drive") val drive: String?,
    @SerializedName("total_days") val total_days: Int?,
    @SerializedName("total_amount") val total_amount: Int?,
    @SerializedName("car_name") val car_name: String?,
    @SerializedName("user_name") val user_name: String?,
    @SerializedName("booked_by") val booked_by: String?,
    @SerializedName("booking_datetime") val booking_datetime: String?,
    @SerializedName("pickup_datetime") val pickup_datetime: String?,
    @SerializedName("return_datetime") val return_datetime: String?
)