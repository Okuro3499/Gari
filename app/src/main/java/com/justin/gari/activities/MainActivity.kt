package com.justin.gari.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.adapters.CarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.models.carModels.CarModel
import com.justin.gari.models.uploadImagesModel.SingleClientImageInfoResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var apiClient: ApiClient
    private val sharedPrefFile = "sharedPrefData"
    private var theme: Switch? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()==true){
            setTheme(R.style.DarkGari)
        } else
            setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, MODE_PRIVATE)
        val shimmerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

        swipeRefresh.setOnRefreshListener {
            getAllData()
        }

        apiClient = ApiClient
        apiClient.getApiService(this).getAllCars().enqueue(object : Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                if (response.isSuccessful) {
                    recyclerview.apply {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.visibility = View.GONE;
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = CarAdapter(response.body()!!.cars, context)
                    }
                }
            }

            override fun onFailure(call: Call<CarModel>, t: Throwable) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.visibility = View.GONE;
                Toast.makeText(this@MainActivity, "Check internet connectivity", Toast.LENGTH_LONG)
                    .show()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val client_id = sharedPreferences.getString("client_id", "default")
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        apiClient.getApiService(this).getUserImageInfo(client_id)
            .enqueue(object : Callback<SingleClientImageInfoResponse> {
                override fun onResponse(
                    call: Call<SingleClientImageInfoResponse>,
                    response: Response<SingleClientImageInfoResponse>
                ) {
                    if (response.isSuccessful) {
                        //fetching images to
                        val userProfile =
                            response.body()!!.single_clientInfo.user_photo_url.toString().trim()
                        editor.putString("userPhoto", userProfile)
                        Log.e(
                            "Gideon",
                            "onSuccess: ${response.body()!!.single_clientInfo.user_photo_url}"
                        )
                        editor.apply()
                    }
                }

                override fun onFailure(call: Call<SingleClientImageInfoResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

//        val profileHeader = sharedPreferences.getString("userPhoto", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "default")
        val lastNameHeader = sharedPreferences.getString("last_name", "default")
        val emailHeader = sharedPreferences.getString("email", "default")
        val header: View = navView.getHeaderView(0)
        val profileImage = header.findViewById(R.id.profile_image) as CircleImageView
        val firstNameTv = header.findViewById<View>(R.id.firstName) as TextView
        val lastNameTv = header.findViewById<View>(R.id.lastName) as TextView
        val emailTv = header.findViewById<View>(R.id.email) as TextView
        firstNameTv.text = firstNameHeader.toString()
        lastNameTv.text = lastNameHeader.toString()
        emailTv.text = emailHeader.toString()
        Picasso.get()
            .load(sharedPreferences.getString("userPhoto", "default"))
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        val switchTheme = header.findViewById(R.id.themeSwitch) as Switch
        if (settingsManager.loadNightModeState()==true){
            switchTheme!!.isChecked=true
        }
        switchTheme!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            } else {
                settingsManager.setNightModeState(false)
                restartApp()
            }
        }

        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(TAG, "onNavigationItemSelected: " + item.itemId)
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
                    val intentMyVehicles = Intent(this@MainActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles)
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
            drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(TAG, "onNavigationItemSelected: nothing clicked")
            false
        })
    }

    private fun restartApp() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun getAllData() {
        if (swipeRefresh.isRefreshing) {
            swipeRefresh.isRefreshing = false

            apiClient = ApiClient
            apiClient.getApiService(this).getAllCars().enqueue(object : Callback<CarModel> {
                override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                    if (response.isSuccessful) {
                        recyclerview.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = CarAdapter(response.body()!!.cars, context)
                        }
                    }
                }

                override fun onFailure(call: Call<CarModel>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Check internet connectivity",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}
