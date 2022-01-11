package com.justin.gari.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.api.ApiService
import com.justin.gari.models.SingleCarModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val carNameTextView = findViewById<TextView>(R.id.tvCarName)
//        val driveOptionTextView = findViewById<TextView>(R.id.tvDriveOption)
        val transmissionTextView = findViewById<TextView>(R.id.tvTransmission)
        val priceTextView = findViewById<TextView>(R.id.tvPrice)
        val engineTextView = findViewById<TextView>(R.id.tvEngine)
        val colorTextView = findViewById<TextView>(R.id.tvColor)
        val registrationTextView = findViewById<TextView>(R.id.tvRegistration)
        val passengersTextView = findViewById<TextView>(R.id.tvPassengers)
        val companyTextView = findViewById<TextView>(R.id.tvCompany)
        val doorsTextView = findViewById<TextView>(R.id.tvDoors)
        val statusTextView = findViewById<TextView>(R.id.tvStatus)
//      val statusTextView = findViewById<TextView>(R.id.tvStatus)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val button = findViewById<Button>(R.id.btBook)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

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


        val carId = intent.getStringExtra("car_id")
        val carName = intent.getStringExtra("car_name")
        val status = intent.getStringExtra("status")
        val transmission = intent.getStringExtra("transmission")
        val engine = intent.getStringExtra("engine")
        val color = intent.getStringExtra("color")
        val registration = intent.getStringExtra("registration")
        val passengers = intent.getStringExtra("passengers")
        val company = intent.getStringExtra("company")
        val price = intent.getStringExtra("price")
        val doors = intent.getStringExtra("doors")

        val apiClient = ApiClient.buildService(ApiService::class.java)
        apiClient.getCarDetails(carId).enqueue(object : Callback<SingleCarModel> {
            override fun onResponse(
                call: Call<SingleCarModel>,
                response: Response<SingleCarModel>
            ) {
                if (response.isSuccessful) {
                    carNameTextView.text = carName
                    statusTextView.text = status
                    transmissionTextView.text = transmission
                    engineTextView.text = engine
                    colorTextView.text = color
                    registrationTextView.text = registration
                    passengersTextView.text = passengers
                    companyTextView.text = company
                    priceTextView.text = price
                    doorsTextView.text = doors
                }
                d("Gideon", "onSuccess: ${response.body()!!.single_car}")
            }

            override fun onFailure(call: Call<SingleCarModel>, t: Throwable) {
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })

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