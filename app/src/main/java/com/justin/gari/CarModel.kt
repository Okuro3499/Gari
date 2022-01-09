package com.justin.gari

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CarModel(
    @Expose
    @SerializedName("cars")
    var cars: List<Cars>
)

