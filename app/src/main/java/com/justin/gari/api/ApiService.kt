package com.justin.gari.api

import com.justin.gari.models.CarModel
import com.justin.gari.models.SingleCarModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>

    @GET("/api/v1/cars/{car_id}")
    fun getCarDetails(@Path("car_id") car_id: String?): Call<SingleCarModel>
}