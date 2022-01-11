package com.justin.gari.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.adapters.CarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.api.ApiService
import com.justin.gari.models.CarModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val shimmerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

        val apiClient = ApiClient.buildService(ApiService::class.java)
        apiClient.getAllCars().enqueue(object : Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                if (response.isSuccessful) {
                    Log.e("Gideon", "onSuccess: ${response.body()}")

//                    shimmerFrameLayout.stopShimmerAnimation()
//                    ShimmerFrameLayout.visibility = View.GONE

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
                R.id.help -> Toast.makeText(applicationContext, "Clicked Help", Toast.LENGTH_SHORT)
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