package com.justin.gari.activities

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.adapters.CarAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityMainBinding
import com.justin.gari.models.carModels.CarModel
import com.justin.gari.models.roleModels.GetRolesResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var apiClient: ApiClient
    lateinit var binding: ActivityMainBinding
    var pref: SharedPrefManager? = null
    var carsAdapter: CarAdapter? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref?.loadNightModeState()}")
        if (pref?.loadNightModeState() == true) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        binding.swipeRefresh.setOnRefreshListener {
            getAllData()
        }

        getAllCars()
        roles()

        val profileHeader = pref?.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val outHeader = binding.outNavView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<Switch>(R.id.themeSwitch)
        val outSwitch = outHeader.findViewById<Switch>(R.id.themeSwitch)
        firstNameTextView.text = "${pref?.getFIRSTNAME()}"
        val firstName = firstNameTextView.text
        emailTextView.text = pref?.getEMAIL()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        if (pref?.loadNightModeState() == true) {
            inSwitch.isChecked = true
            outSwitch.isChecked = true
            inSwitch.text = getString(R.string.light_mode)
            outSwitch.text = getString(R.string.light_mode)
        } else{
            inSwitch.text = getString(R.string.dark_mode)
            outSwitch.text = getString(R.string.dark_mode)
        }

        inSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref?.setNightModeState(true)
                pref?.setSWITCHEDTHEME(true)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            } else {
                pref?.setNightModeState(false)
                pref?.setSWITCHEDTHEME(false)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            }
        }

        outHeader.findViewById<Switch>(R.id.themeSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref?.setNightModeState(true)
                pref?.setSWITCHEDTHEME(true)
                outSwitch.text = getString(R.string.dark_mode)
                restartApp()
            } else {
                pref?.setNightModeState(false)
                pref?.setSWITCHEDTHEME(false)
                outSwitch.text = getString(R.string.dark_mode)
                restartApp()
            }
        }

        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)

        Log.d("timeOfDay", timeOfDay.toString() + "")
        if (timeOfDay < 12) {
            binding.greeting.setText(R.string.morning)
        } else if (timeOfDay < 16) {
            binding.greeting.setText(R.string.afternoon)
        } else if (timeOfDay < 21) {
            binding.greeting.setText(R.string.evening)
        } else {
            binding.greeting.setText(R.string.night)
        }

        Log.d("ghj", "$firstName ")
        if (pref?.getFIRSTNAME() != "") {
            binding.name.text = getString(R.string.username, "$firstName")
        }

        if ("$firstName" != "") {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
            binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(TAG, "onNavigationItemSelected: " + item.itemId)

                when (item.itemId) {
                    R.id.home -> {
                        val intentHome = Intent(this, MainActivity::class.java)
                        startActivity(intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.profile -> {
                        val intentProfile = Intent(this, UserProfileActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.myVehicles -> {
                        val intentMyVehicles = Intent(this, VehiclesActivity::class.java)
                        startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.logout -> {
                        pref?.clearAllDataExcept()
                        val intentLogout = Intent(this, MainActivity::class.java)
                        startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        finish()
                        return@OnNavigationItemSelectedListener true

                    }
                    R.id.about -> {
                        val intentAbout = Intent(this, AboutActivity::class.java)
                        startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.help -> {
                        val intentHelp = Intent(this, LoginActivity::class.java)
                        startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                Log.i(TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        }
        else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START)
            binding.outNavView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(TAG, "onNavigationItemSelected: " + item.itemId)
                when (item.itemId) {
                    R.id.login -> {
                        val intentLogin = Intent(this, LoginActivity::class.java)
                        startActivity(intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.createAccount -> {
                        val intentProfile = Intent(this, RegisterActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.about -> {
                        val intentAbout = Intent(this, AboutActivity::class.java)
                        startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.help -> {
                        val intentHelp = Intent(this, LoginActivity::class.java)
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
            if ("$firstName" != "") {
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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                carsAdapter?.filter?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                carsAdapter?.filter?.filter(newText)
                return true
            }
        })
    }

    private fun getAllCars() {
        binding.shimmerLayout.startShimmer()
        apiClient.getApiService().getAllCars().enqueue(object : Callback<CarModel> {
            override fun onResponse(call: Call<CarModel>, response: Response<CarModel>) {
                Log.e("id", response.toString())
                if (response.isSuccessful) {
                    carsAdapter = response.body()?.cars?.let {
                        CarAdapter(it, this@MainActivity)
                    }
                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.recyclerview.adapter = carsAdapter
                }
            }

            override fun onFailure(call: Call<CarModel>, t: Throwable) {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.errorPage.visibility = View.VISIBLE
                binding.message.text = t.message
                binding.searchView.visibility = View.GONE
                binding.swipeRefresh.visibility = View.GONE

                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun roles() {
        apiClient.getApiService().getRoles("2").enqueue(object : Callback<GetRolesResponse> {
            override fun onResponse(call: Call<GetRolesResponse>, response: Response<GetRolesResponse>) {
                Log.e("id", response.toString())
                if (response.isSuccessful) {
                    pref?.setROLEID(response.body()?.role_details?.roleId.toString())
                    pref?.setROLENAME(response.body()?.role_details?.roleName)
                    pref?.setROLEDESCRIPTION(response.body()?.role_details?.roleDescription)
                }
            }

            override fun onFailure(call: Call<GetRolesResponse>, t: Throwable) {
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun restartApp() {
        finish()
        startActivity(intent)
    }

    private fun getAllData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
            getAllCars()
        }
    }
}