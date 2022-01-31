package com.justin.gari.models.bookingCarModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BookCarResponse(
    @Expose
    @SerializedName("book_car") val book_car: BookCarResponseObject
)