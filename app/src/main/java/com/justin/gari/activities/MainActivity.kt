package com.justin.gari.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.adapters.CarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityMainBinding
import com.justin.gari.models.carModels.CarModel
import com.justin.gari.utils.SettingsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var apiClient: ApiClient
    lateinit var settingsManager: SettingsManager
    lateinit var binding: ActivityMainBinding
    val sharedPrefFile = "sharedPrefData"

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        binding.swipeRefresh.setOnRefreshListener {
            getAllData()
        }

        getAllCars()

//        val client_id = sharedPreferences.getString("client_id", "default")
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        apiClient.getApiService(this).getUserImageInfo(client_id).enqueue(object : Callback<SingleClientImageInfoResponse> {
//                override fun onResponse(call: Call<SingleClientImageInfoResponse>, response: Response<SingleClientImageInfoResponse>) {
//                    if (response.isSuccessful) {
//                        //fetching images to
//                        val userProfile = response.body()!!.single_clientInfo.user_photo_url.toString().trim()
//                        editor.putString("userPhoto", userProfile)
//                        Log.e("Gideon", "onSuccess: ${response.body()!!.single_clientInfo.user_photo_url}")
//                        editor.apply()
//                    }
//                }
//
//                override fun onFailure(call: Call<SingleClientImageInfoResponse>, t: Throwable) {
//                    Log.e("Gideon", "onFailure: ${t.message}")
//                }
//            })

        val profileHeader = sharedPreferences.getString("userProfile", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "")
        val lastNameHeader = sharedPreferences.getString("last_name", "")
        val emailHeader = sharedPreferences.getString("email", "")
        val outHeader = binding.outNavView.getHeaderView(0)
        val header = binding.navView.getHeaderView(0)
        header.firstName.text = firstNameHeader.toString()
        header.lastName.text = lastNameHeader.toString()
        header.email.text = emailHeader.toString()
        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(header.profile_image)

        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        Log.d("timeOfDay", timeOfDay.toString() + "")
        if (timeOfDay < 12) {
            binding.greeting.setText(R.string.morning)
        }
        else if (timeOfDay < 16) {
            binding.greeting.setText(R.string.afternoon)
        }
        else if (timeOfDay < 21) {
            binding.greeting.setText(R.string.evening)
        }
        else {
            binding.greeting.setText(R.string.night)
        }

        if (firstNameHeader != "") {
            binding.name.text = ", $firstNameHeader"
        }

        if (settingsManager.loadNightModeState()) {
            header.themeSwitch!!.isChecked = true
        }
        header.themeSwitch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            } else {
                settingsManager.setNightModeState(false)
                restartApp()
            }
        }

        outHeader.themeSwitch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            } else {
                settingsManager.setNightModeState(false)
                restartApp()
            }
        }

        if (firstNameHeader != "") {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
            binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(TAG, "onNavigationItemSelected: " + item.itemId)

                when (item.itemId) {
                    R.id.home -> {
                        startActivity(
                            Intent(this@MainActivity, MainActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                        )
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.profile -> {
                        val intentProfile =
                            Intent(this@MainActivity, UserProfileActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.myVehicles -> {
                        val intentMyVehicles =
                            Intent(this@MainActivity, VehiclesActivity::class.java)
                        startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.logout -> {
                        editor.clear()
                        editor.apply()
                        val intentLogout = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        finish()
                        return@OnNavigationItemSelectedListener true

                    }
                    R.id.about -> {
                        val intentAbout = Intent(this@MainActivity, AboutActivity::class.java)
                        startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.help -> {
                        val intentHelp = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                Log.i(TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        } else {
            binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.START
            )
            binding.outNavView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(TAG, "onNavigationItemSelected: " + item.itemId)

                when (item.itemId) {
                    R.id.login -> {
                        startActivity(
                            Intent(this@MainActivity, LoginActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                            )
                        )
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.createAccount -> {
                        val intentProfile = Intent(this@MainActivity, RegisterActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.about -> {
                        val intentAbout = Intent(this@MainActivity, AboutActivity::class.java)
                        startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.help -> {
                        val intentHelp = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                Log.i(TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        }

        binding.nav.setOnClickListener {
            if (firstNameHeader != "") {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        binding.refresh.setOnClickListener {
            binding.errorPage.visibility = View.GONE
            binding.shimmerLayout.visibility = View.VISIBLE
            getAllData()
        }
    }

    private fun getAllCars() {
        binding.shimmerLayout.startShimmer();
        apiClient = ApiClient
        apiClient.getApiService(this).getAllCars().enqueue(object : Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                if (response.isSuccessful) {
                    val carsAdapter = CarAdapter(response.body()!!.cars, this@MainActivity)
                    binding.shimmerLayout.stopShimmer();
                    binding.shimmerLayout.visibility = View.GONE;
                    binding.recyclerview.adapter = carsAdapter
                }
            }

            override fun onFailure(call: Call<CarModel>, t: Throwable) {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.errorPage.visibility = View.VISIBLE
                binding.message.text = t.message
                binding.etSearch.visibility = View.GONE
                binding.swipeRefresh.visibility = View.GONE

//                Toast.makeText(this@MainActivity, "Check internet connectivity", Toast.LENGTH_LONG).show()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun restartApp() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun getAllData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false

            getAllCars()
        }
    }
}
