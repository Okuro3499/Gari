package com.justin.gari.models.carModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SingleCarModel(
    @Expose
    @SerializedName("single_car")
    var single_car: List<Cars>
)