package com.justin.gari.models.saveCarModels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SavedCarResponse(
    @Expose
    @SerializedName("saved_cars") val saved_cars: List<SaveCarObject>
)