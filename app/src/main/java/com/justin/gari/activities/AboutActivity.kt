package com.justin.gari.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityAboutBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.nav_header.view.*

class AboutActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityAboutBinding
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState() == true) {
            setTheme(R.style.DarkGari)
        }
        else setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        apiClient = ApiClient

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        val client_id = sharedPreferences.getString("client_id", "default")
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        apiClient.getApiService(this).getUserImageInfo(client_id).enqueue(
//            object : Callback<SingleClientImageInfoResponse> {
//                override fun onResponse(call: Call<SingleClientImageInfoResponse>, response: Response<SingleClientImageInfoResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        //fetching images to
//                        //TODO: fix crash when value is null
////                            val userProfile = response.body()!!.single_clientInfo.user_photo_url.toString().trim()
////                            editor.putString("userPhoto", userProfile)
////                            editor.apply()
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<SingleClientImageInfoResponse>, t: Throwable) {
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
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@AboutActivity, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this@AboutActivity, ProfileCompleteActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
//                    val intentMyVehicles = Intent(this@AboutActivity, VehiclesActivity::class.java)
//                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@AboutActivity, LoginActivity::class.java)
                    startActivity(intentLogin)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@AboutActivity, AboutActivity::class.java)
                    startActivity(intentAbout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@AboutActivity, LoginActivity::class.java)
                    startActivity(intentHelp)
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })
    }

    private fun restartApp() {
        val i = Intent(applicationContext, AboutActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}