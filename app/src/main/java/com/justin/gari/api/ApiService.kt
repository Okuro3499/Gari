package com.justin.gari.api

import com.justin.gari.CarModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>
}