package com.justin.gari.models.bookingCarModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BookingsResponse(
    @Expose
    @SerializedName("myBooked_cars") val myBooked_cars: List<BookingsResponseObject>
)