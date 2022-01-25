package com.justin.gari.api

import com.justin.gari.models.carModels.CarModel
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import com.justin.gari.models.userModels.signUpModel.NewUserData
import com.justin.gari.models.userModels.signUpModel.NewUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>

    @GET("/api/v1/cars/{car_id}")
    fun getCarDetails(@Path("car_id") car_id: String?): Call<SingleCarModel>

    @POST("api/v1/auth/login/client")
    fun loginUser(@Body userLogin: UserLogin?): Call<UserLoginResponse>

    @POST("/api/v1/auth/register/client")
    fun createUser(@Body newUser: NewUserData?): Call<NewUserResponse>

    @GET("/api/v1/client/{client_id}")
    fun getUserDetails(@Path("client_id") client_id: String?): Call<UserDetailsResponse>

    @POST("/api/v1/save")
    fun saveCar(@Body saveCar: SaveCar?): Call<SaveCarResponse>
}