package com.justin.gari.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val carNameTextView = findViewById<TextView>(R.id.tvCarName)
        val driveOptionTextView = findViewById<TextView>(R.id.tvDriveOption)
        val transmissionTextView = findViewById<TextView>(R.id.tvTransmission)
        val priceTextView = findViewById<TextView>(R.id.tvPrice)
        val engineTextView = findViewById<TextView>(R.id.tvEngine)
        val colorTextView = findViewById<TextView>(R.id.tvColor)
        val registrationTextView = findViewById<TextView>(R.id.tvRegistration)
        val passengersTextView = findViewById<TextView>(R.id.tvPassengers)
        val companyTextView = findViewById<TextView>(R.id.tvCompany)
        val doorsTextView = findViewById<TextView>(R.id.tvDoors)
        val statusTextView = findViewById<TextView>(R.id.tvStatus)
        val feature1TextView = findViewById<TextView>(R.id.tvFeature1)
        val feature2TextView = findViewById<TextView>(R.id.tvFeature2)
        val feature3TextView = findViewById<TextView>(R.id.tvFeature3)
        val feature4TextView = findViewById<TextView>(R.id.tvFeature4)
        val feature5TextView = findViewById<TextView>(R.id.tvFeature5)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)


        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.premio))
        imageList.add(SlideModel(R.drawable.rear))
        imageList.add(SlideModel(R.drawable.seats))
        imageList.add(SlideModel(R.drawable.dash))
        imageList.add(SlideModel(R.drawable.speedometer))
        imageList.add(SlideModel(R.drawable.pre))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //receiving intents
        val carId = intent.getStringExtra("car_id")

        apiClient = ApiClient
        apiClient.getApiService(this).getCarDetails(carId)
            .enqueue(object : Callback<SingleCarModel> {
                override fun onResponse(
                    call: Call<SingleCarModel>,
                    response: Response<SingleCarModel>
                ) {
                    if (response.isSuccessful) {
                        carNameTextView.text = response.body()!!.single_car.car_name.toString()
                        statusTextView.text = response.body()!!.single_car.status.toString()
                        transmissionTextView.text =
                            response.body()!!.single_car.transmission.toString()
                        engineTextView.text = response.body()!!.single_car.engine.toString()
                        colorTextView.text = response.body()!!.single_car.color.toString()
                        registrationTextView.text =
                            response.body()!!.single_car.registration.toString()
                        passengersTextView.text = response.body()!!.single_car.passengers.toString()
                        companyTextView.text = response.body()!!.single_car.company.toString()
                        priceTextView.text = response.body()!!.single_car.price.toString()
                        doorsTextView.text = response.body()!!.single_car.doors.toString()
                        driveOptionTextView.text = response.body()!!.single_car.drive.toString()
                        feature1TextView.text = response.body()!!.single_car.feature_1.toString()
                        feature2TextView.text = response.body()!!.single_car.feature_2.toString()
                        feature3TextView.text = response.body()!!.single_car.feature_3.toString()
                        feature4TextView.text = response.body()!!.single_car.feature_4.toString()
                        feature5TextView.text = response.body()!!.single_car.feature_5.toString()
                    }
                }

                override fun onFailure(call: Call<SingleCarModel>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        val saveBt = findViewById<ImageButton>(R.id.ibSave)
        saveBt.setOnClickListener {
            val car_id = carId
            val client_id = sharedPreferences.getString("client_id", "default")
            val saveInfo = SaveCar(car_id, client_id)

            apiClient.getApiService(this).saveCar(saveInfo)
                .enqueue(object : Callback<SaveCarResponse> {
                    override fun onResponse(
                        call: Call<SaveCarResponse>, response: Response<SaveCarResponse>
                    ) {

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DetailActivity,
                                "Saved Successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            Log.e("Gideon", "onSuccess: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<SaveCarResponse>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_LONG)
                            .show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }

        val bookBt = findViewById<Button>(R.id.btBook)
        bookBt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT)
                    .show()
                R.id.profile -> Toast.makeText(
                    applicationContext,
                    "Clicked Profile",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.myVehicles -> Toast.makeText(
                    applicationContext,
                    "Clicked My Vehicles",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.logout -> Toast.makeText(
                    applicationContext,
                    "Clicked Logout",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.about -> Toast.makeText(
                    applicationContext,
                    "Clicked About",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.help -> Toast.makeText(
                    applicationContext,
                    "Clicked Help",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}