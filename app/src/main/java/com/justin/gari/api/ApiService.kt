package com.justin.gari.api

import com.justin.gari.models.bookingCarModels.BookCar
import com.justin.gari.models.bookingCarModels.BookCarResponse
import com.justin.gari.models.bookingCarModels.BookingsResponse
import com.justin.gari.models.carModels.CarModel
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.roleModels.GetRolesResponse
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import com.justin.gari.models.saveCarModels.SavedCarResponse
import com.justin.gari.models.uploadImagesModel.*
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import com.justin.gari.models.userModels.signUpModel.NewUserData
import com.justin.gari.models.userModels.signUpModel.NewUserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/api/v1/cars")
    fun getAllCars(): Call<CarModel>

    @GET("/api/v1/cars/{car_id}")
    fun getCarDetails(@Path("car_id") car_id: String?): Call<SingleCarModel>

    @POST("/api/v1/auth/login")
    fun loginUser(@Body userLogin: UserLogin?): Call<UserLoginResponse>

    @GET("/api/v1/roles/{role_id}")
    fun getRoles(@Path("role_id") role_id: String?): Call<GetRolesResponse>

    @POST("/api/v1/auth/register")
    fun createUser(@Body newUser: NewUserData?): Call<NewUserResponse>

    @GET("/api/v1/users/userDetails/{user_id}/{role_id}")
    fun getUserDetails(@Path("user_id") user_id: String?, @Path("role_id") role_id: String?): Call<UserDetailsResponse>

    @POST("/api/v1/save")
    fun saveCar(@Body saveCar: SaveCar?): Call<SaveCarResponse>

    @GET("/api/v1/saved/{user_id}")
    fun getSavedCars(@Path("user_id") user_id: String?): Call<SavedCarResponse>

    @GET("/api/v1/clientBookings/{user_id}")
    fun getBookedCars(@Path("user_id") user_id: String?): Call<BookingsResponse>

    @POST("/api/v1/booking")
    fun bookingCar(@Body bookCar: BookCar?): Call<BookCarResponse>

    @PATCH("/api/v1/cars/status/{car_id}")
    fun changeStatus(@Path("car_id") car_id: String?)

    @PUT("/api/v1/contact/{user_id}")
    fun contactUpdate(@Path("user_id") user_id: String?, @Body contacts: Contacts?): Call<UserDetailsResponse>

    @Multipart
    @POST("/api/v1/driverLicense/uploadCloudinary")
    fun dlCloudinary(@Part image: MultipartBody.Part): Call<DlCloudinaryResponse>

    @PUT("/api/v1/dbDl/{user_id}")
    fun dlCloudinaryResponseToDb(
        @Path("user_id") user_id: String?,
        @Body dlurl: DlUrl?
    ): Call<UserDetailsResponse>

    @Multipart
    @POST("/api/v1/nationalId/uploadCloudinary")
    fun idCloudinary(@Part image: MultipartBody.Part): Call<IdCloudinaryResponse>

    @Multipart
    @POST("/api/v1/userPhoto/uploadCloudinary")
    fun userPhotoCloudinary(@Part image: MultipartBody.Part): Call<UserPhotoCloudinaryResponse>

    @PUT("/api/v1/dbnId/{user_id}")
    fun nationalIdCloudinaryResponseToDb(
        @Path("user_id") user_id: String?,
        @Body nationalIdUrl: NationalIdUrl
    ): Call<UserDetailsResponse>

    @PUT("/api/v1/usPdb/{user_id}")
    fun userPhotoCloudinaryResponseToDb(
        @Path("user_id") user_id: String?,
        @Body userPhotoUrl: UserPhotoUrl
    ): Call<UserDetailsResponse>
}