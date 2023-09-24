package com.justin.gari.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityAboutBinding
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class AboutActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityAboutBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref!!.loadNightModeState()}")
        if (pref!!.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

//        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()

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

        val profileHeader = pref!!.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val outHeader = binding.outNavView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<Switch>(R.id.themeSwitch)
        val outSwitch = outHeader.findViewById<Switch>(R.id.themeSwitch)
        firstNameTextView.text = "${pref!!.getFIRSTNAME()}"
        val firstName = firstNameTextView.text
        emailTextView.text = pref!!.getEMAIL()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        if (pref!!.loadNightModeState()) {
            inSwitch.isChecked = true
            outSwitch.isChecked = true
            inSwitch.text = getString(R.string.light_mode)
            outSwitch.text = getString(R.string.light_mode)
        } else {
            inSwitch.text = getString(R.string.dark_mode)
            outSwitch.text = getString(R.string.dark_mode)
        }

        inSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref!!.setNightModeState(true)
                pref!!.setSWITCHEDTHEME(true)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            } else {
                pref!!.setNightModeState(false)
                pref!!.setSWITCHEDTHEME(false)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            }
        }

        outHeader.findViewById<Switch>(R.id.themeSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref!!.setNightModeState(true)
                pref!!.setSWITCHEDTHEME(true)
                outSwitch.text = getString(R.string.dark_mode)
                restartApp()
            } else {
                pref!!.setNightModeState(false)
                pref!!.setSWITCHEDTHEME(false)
                outSwitch.text = getString(R.string.dark_mode)
                restartApp()
            }
        }

        if ("$firstName" != "") {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
            binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)

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
                        pref!!.clearAllDataExcept()
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
                Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START)
            binding.outNavView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)

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
                Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
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

        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun restartApp() {
        finish()
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}