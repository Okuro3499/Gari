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
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.utils.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityEmergencyContactsBinding
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmergencyContactsActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmergencyContactsBinding
    var pref: SharedPrefManager? = null
    private lateinit var apiClient: ApiClient
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiClient = ApiClient
        pref = SharedPrefManager(this)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val userId = pref!!.getUSERID()
        val roleId = pref!!.getROLEID()

//        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val profileHeader = pref!!.getUSERPROFILEPHOTO()
        val firstNameHeader = pref!!.getFIRSTNAME()
        val lastNameHeader = pref!!.getLASTNAME()
        val emailHeader = pref!!.getEMAIL()
        val header: View = binding.navView.getHeaderView(0)
        val profileImage = header.findViewById(R.id.profile_image) as CircleImageView
        val firstNameTv = header.findViewById<View>(R.id.firstName) as TextView
        firstNameTv.text = firstNameHeader.toString()
        val lastNameTv = header.findViewById<View>(R.id.lastName) as TextView
        lastNameTv.text = lastNameHeader.toString()
        val emailTv = header.findViewById<View>(R.id.email) as TextView
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
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            when (item.itemId) {
                R.id.home -> {
                    startActivity(
                        Intent(this@EmergencyContactsActivity, MainActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@EmergencyContactsActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles =
                        Intent(this@EmergencyContactsActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    pref!!.clearAllDataExcept()
                    val intentLogout = Intent(this@EmergencyContactsActivity, MainActivity::class.java)
                    startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout =
                        Intent(this@EmergencyContactsActivity, AboutActivity::class.java)
                    startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp =
                        Intent(this@EmergencyContactsActivity, LoginActivity::class.java)
                    startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

        binding.back.setOnClickListener {
            val intent = Intent(this@EmergencyContactsActivity, UserProfileActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        apiClient = ApiClient
        apiClient.getApiService(this).getUserDetails(userId, roleId).enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                if (response.isSuccessful) {
                    Log.e("Gideon", "onSuccess: ${response.body()}")

                    binding.etFullName1.setText(
                        if(response.body()!!.single_user.contact1_name.toString() == "null") {
                            ""
                        } else{
                            response.body()!!.single_user.contact1_name.toString()
                        })
                    binding.etRelationShip1.setText(
                        if(response.body()!!.single_user.contact1_relationship.toString() == "null"){
                            ""
                        } else{
                            response.body()!!.single_user.contact1_relationship.toString()
                        })
                    binding.etEmergencyMobile1.setText(
                        if(response.body()!!.single_user.contact1_mobile.toString() == "null"){
                            ""
                        } else{
                            response.body()!!.single_user.contact1_mobile.toString()
                        })
                    binding.etFullName2.setText(
                        if(response.body()!!.single_user.contact2_name.toString() == "null"){
                            ""
                        } else{
                            response.body()!!.single_user.contact2_name.toString()
                        })
                    binding.etRelationShip2.setText(
                        if(response.body()!!.single_user.contact2_relationship.toString() == "null"){
                            ""
                        } else{
                            response.body()!!.single_user.contact2_relationship.toString()
                        })
                    binding.etEmergencyMobile2.setText(
                        if(response.body()!!.single_user.contact1_mobile.toString() == "null"){
                            ""
                        } else{
                            response.body()!!.single_user.contact1_mobile.toString()
                        })
                }
            }

            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })

    }

    private fun restartApp() {
        recreate()
    }
}