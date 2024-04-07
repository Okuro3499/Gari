package com.justin.gari.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.justin.gari.R
import com.justin.gari.adapters.MyVehiclesAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityVehiclesBinding
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class VehiclesActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    lateinit var pref: SharedPrefManager
    lateinit var binding: ActivityVehiclesBinding
    private var adapter: MyVehiclesAdapter? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref.loadNightModeState()}")
        if (pref.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)

        binding = ActivityVehiclesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

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

        val profileHeader = pref.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val lastNameTextView = header.findViewById<TextView>(R.id.lastName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<SwitchCompat>(R.id.themeSwitch)
        firstNameTextView.text = pref.getFIRSTNAME()
        lastNameTextView.text = pref.getLASTNAME()
        emailTextView.text = pref.getEMAIL()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        if (pref.loadNightModeState()) {
            inSwitch.isChecked = true
            inSwitch.text = getString(R.string.light_mode)
        } else{
            inSwitch.text = getString(R.string.dark_mode)
        }

        inSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.setNightModeState(true)
                pref.setSWITCHEDTHEME(true)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            } else {
                pref.setNightModeState(false)
                pref.setSWITCHEDTHEME(false)
                inSwitch.text = getString(R.string.light_mode)
                restartApp()
            }
        }

        binding.nav.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

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
                    pref.clearAllDataExcept()
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

        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
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
        finish()
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}