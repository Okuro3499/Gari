package com.justin.gari.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.justin.gari.R
import com.justin.gari.adapters.MyVehiclesAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityVehiclesBinding
import com.justin.gari.utils.SettingsManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class VehiclesActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private val sharedPrefFile = "sharedPrefData"
    private var theme: Switch? = null
    private lateinit var settingsManager: SettingsManager
    lateinit var binding: ActivityVehiclesBinding
    private var adapter: MyVehiclesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else
            setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)


        binding = ActivityVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        apiClient = ApiClient

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Bookings"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Saved"))

        val fragmentManager = supportFragmentManager
        adapter = MyVehiclesAdapter(fragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        val profileHeader = sharedPreferences.getString("userProfile", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "")
        val lastNameHeader = sharedPreferences.getString("last_name", "")
        val emailHeader = sharedPreferences.getString("email", "")
        val header: View = binding.navView.getHeaderView(0)
        val profileImage = header.findViewById(R.id.profile_image) as CircleImageView
        val firstNameTv = header.findViewById<View>(R.id.firstName) as TextView
        val lastNameTv = header.findViewById<View>(R.id.lastName) as TextView
        val emailTv = header.findViewById<View>(R.id.email) as TextView
        firstNameTv.text = firstNameHeader.toString()
        lastNameTv.text = lastNameHeader.toString()
        emailTv.text = emailHeader.toString()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        val switchTheme = header.findViewById(R.id.themeSwitch) as Switch
        if (settingsManager.loadNightModeState()) {
            switchTheme.isChecked = true
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            } else {
                settingsManager.setNightModeState(false)
                restartApp()
            }
        }

        binding.nav.setOnClickListener {
            if (firstNameHeader != "") {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@VehiclesActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@VehiclesActivity, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this@VehiclesActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles = Intent(this@VehiclesActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    editor.clear()
                    editor.apply()
                    val intentLogout = Intent(this@VehiclesActivity, MainActivity::class.java)
                    startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@VehiclesActivity, AboutActivity::class.java)
                    startActivity(intentAbout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@VehiclesActivity, AboutActivity::class.java)
                    startActivity(intentHelp)
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })
    }

//    private fun getUserImageInfo() {
//        val sharedPreferences: SharedPreferences =
//            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val client_id = sharedPreferences.getString("client_id", "default")
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        apiClient.getApiService(this).getUserImageInfo(client_id)
//            .enqueue(object : Callback<SingleClientImageInfoResponse> {
//                override fun onResponse(
//                    call: Call<SingleClientImageInfoResponse>,
//                    response: Response<SingleClientImageInfoResponse>
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
//                override fun onFailure(call: Call<SingleClientImageInfoResponse>, t: Throwable) {
//                    Log.e("Gideon", "onFailure: ${t.message}")
//                }
//            })
//    }

    private fun restartApp() {
        val i = Intent(applicationContext, VehiclesActivity::class.java)
        startActivity(i)
        finish()
    }
}