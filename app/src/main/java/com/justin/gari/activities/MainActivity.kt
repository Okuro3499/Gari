package com.justin.gari.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.adapters.CarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityMainBinding
import com.justin.gari.models.carModels.CarModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var apiClient: ApiClient
    lateinit var settingsManager: SettingsManager
    lateinit var binding: ActivityMainBinding
    val sharedPrefFile = "sharedPrefData"
//    lateinit var adap: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState() == true) {
            setTheme(R.style.DarkGari)
        }
        else setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        binding.shimmerLayout.startShimmer();

        binding.swipeRefresh.setOnRefreshListener {
            getAllData()
        }

        getAllCars()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        val firstNameHeader = sharedPreferences.getString("first_name", "default")
        val lastNameHeader = sharedPreferences.getString("last_name", "default")
        val emailHeader = sharedPreferences.getString("email", "default")
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

        if (settingsManager.loadNightModeState() == true) {
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

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(TAG, "onNavigationItemSelected: " + item.itemId)
            //TODO: set visibility
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@MainActivity, ProfileCompleteActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
//                    val intentMyVehicles = Intent(this@MainActivity, VehiclesActivity::class.java)
//                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intentLogin)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@MainActivity, AboutActivity::class.java)
                    startActivity(intentAbout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intentHelp)
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(TAG, "onNavigationItemSelected: nothing clicked")
            false
        })
    }

    private fun getAllCars() {
        apiClient = ApiClient
        apiClient.getApiService(this).getAllCars().enqueue(object : Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                if (response.isSuccessful) {
                    val carsAdapter = CarAdapter(response.body()!!.cars, this@MainActivity)
                    binding.shimmerLayout.stopShimmer();
                    binding.shimmerLayout.visibility = View.GONE;
                    binding.recyclerview.adapter = carsAdapter

//                    recyclerview.apply {
//                        binding.shimmerLayout.stopShimmer();
//                        binding.shimmerLayout.visibility = View.GONE;
//                        layoutManager = LinearLayoutManager(this@MainActivity)
//                        adapter = CarAdapter(response.body()!!.cars, context)
//                    }
                }
            }

            override fun onFailure(call: Call<CarModel>, t: Throwable) {
                binding.shimmerLayout.stopShimmer();
                binding.shimmerLayout.visibility = View.GONE;
                Toast.makeText(this@MainActivity, "Check internet connectivity", Toast.LENGTH_LONG).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}
