package com.justin.gari.api

import com.justin.gari.models.*
import com.justin.gari.models.carModels.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>

    @GET("/api/v1/cars/{car_id}")
    fun getCarDetails(@Path("car_id") car_id: String?): Call<SingleCarModel>

    @POST("api/v1/auth/login/client")
    fun loginUser(@Body userLogin: UserLogin?): Call<UserLoginResponse>

    @POST("/api/v1/auth/register/client")
    fun createUser(@Body newUser: User?) : Call<NewUserResponse>

    @GET("/api/v1/client/{client_id}")
    fun getUserDetails(@Path("client_id") client_id: String?): Call<UserDetailsResponse>
}