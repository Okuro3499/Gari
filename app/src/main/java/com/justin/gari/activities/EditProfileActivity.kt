package com.justin.gari.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.utils.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityEditProfileBinding
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    lateinit var apiClient: ApiClient
    lateinit var binding: ActivityEditProfileBinding
    var userId :String? = null
    var roleId :String? = null

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref!!.loadNightModeState()}")
        if (pref!!.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        userId = pref!!.getUSERID()
        roleId = pref!!.getROLEID()

        val profileHeader = pref!!.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<Switch>(R.id.themeSwitch)
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
            inSwitch.text = getString(R.string.light_mode)
        } else{
            inSwitch.text = getString(R.string.dark_mode)
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

        binding.back.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
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

        getUserDetails()

    }

    private fun getUserDetails() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Fetching Details..") // set message
        progressDialog.show()

        apiClient.getApiService(this).getUserDetails(userId, roleId).enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    Log.e("Gideon", "onSuccess: ${response.body()}")
                    binding.etFirstName.setText(response.body()!!.single_user.first_name.toString())
                    binding.etLastName.setText(response.body()!!.single_user.last_name.toString())
                    binding.etEmailAddress.setText(response.body()!!.single_user.email.toString())
                    binding.etMobile.setText(response.body()!!.single_user.mobile.toString())
                    binding.etCounty.setText(response.body()!!.single_user.county.toString())
                    binding.etDistrict.setText(response.body()!!.single_user.district.toString())
                    binding.etEstate.setText(response.body()!!.single_user.estate.toString())
                    binding.etLandMark.setText(response.body()!!.single_user.landmark.toString())
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun restartApp() {
        finish()
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}