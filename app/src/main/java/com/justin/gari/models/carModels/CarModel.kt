package com.justin.gari.models.carModels

data class CarModel(
    val cars: List<Car>
)

data class Car(
    val car_id: Int,
    val car_name: String,
    val status: String,
    val transmission: String,
    val color: String,
    val registration: String,
    val passengers: Int,
    val company_id: Int,
    val price: String,
    val doors: String,
    val car_images: List<String>,
    val features: List<String>,
    val cc: String?,
    val fuel: String?,
    val created_by: Int?,
    val created_date: String?,
    val modified_by: Int?,
    val modified_date: String?,
    val drive: List<String>?
)


