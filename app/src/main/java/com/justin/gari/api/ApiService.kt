package com.justin.gari.api

import com.justin.gari.models.NewUserResponse
import com.justin.gari.models.User
import com.justin.gari.models.UserLogin
import com.justin.gari.models.carModels.CarModel
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.UserLoginResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST

interface ApiService {
    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>

    @GET("/api/v1/cars/{car_id}")
    fun getCarDetails(@Path("car_id") car_id: String?): Call<SingleCarModel>

    @POST("api/v1/auth/login/client")
    fun loginUser(@Body userLogin: UserLogin?): Call<UserLoginResponse>

    @POST("/api/v1/auth/register/client")
    fun createUser(@Body newUser: User?) : Call<NewUserResponse>
}