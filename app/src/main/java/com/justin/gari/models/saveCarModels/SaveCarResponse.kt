package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveCarResponse(
    @Expose
    @SerializedName("save_car") val save_car: SaveCarObject
)