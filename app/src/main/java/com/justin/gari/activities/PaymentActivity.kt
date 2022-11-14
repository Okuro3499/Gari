package com.justin.gari.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.utils.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityPaymentBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*

class PaymentActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else
            setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiClient = ApiClient

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val profileHeader = sharedPreferences.getString("userProfile", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "")
        val lastNameHeader = sharedPreferences.getString("last_name", "")
        val emailHeader = sharedPreferences.getString("email", "")
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

        if (settingsManager.loadNightModeState()) {
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

        binding.nav.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            when (item.itemId) {
                R.id.home -> {
                    startActivity(
                        Intent(this@PaymentActivity, MainActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@PaymentActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles =
                        Intent(this@PaymentActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    editor.clear()
                    editor.apply()
                    val intentLogout = Intent(this@PaymentActivity, MainActivity::class.java)
                    startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@PaymentActivity, AboutActivity::class.java)
                    startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@PaymentActivity, LoginActivity::class.java)
                    startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

        binding.back.setOnClickListener {
            val intent = Intent(this@PaymentActivity, UserProfileActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.mpesaButton.setOnClickListener {
            if (binding.mpesa.visibility == View.GONE) {
                binding.mpesa.visibility = View.VISIBLE

                if (binding.visa.visibility == View.VISIBLE) {
                    binding.visa.visibility = View.GONE
                }
                if (binding.masterCard.visibility == View.VISIBLE) {
                    binding.masterCard.visibility = View.GONE
                }
            }
//            else if (binding.mpesa.visibility == View.VISIBLE) {
//                binding.mpesa.visibility = View.GONE
//            }
        }

//        binding.paypalButton.setOnClickListener {
//            if (binding.mpesa.visibility == View.GONE) {
//                binding.mpesa.visibility = View.VISIBLE
//            } else if (binding.mpesa.visibility == View.VISIBLE) {
//                binding.mpesa.visibility = View.GONE
//            }
//        }

        binding.visaButton.setOnClickListener {
            if (binding.visa.visibility == View.GONE) {
                binding.visa.visibility = View.VISIBLE
                if (binding.mpesa.visibility == View.VISIBLE) {
                    binding.mpesa.visibility = View.GONE
                }
                if (binding.masterCard.visibility == View.VISIBLE) {
                    binding.masterCard.visibility = View.GONE
                }
            }
//            else if (binding.visa.visibility == View.VISIBLE) {
//                binding.visa.visibility = View.GONE
//            }
        }

        binding.masterCardButton.setOnClickListener {
            if (binding.masterCard.visibility == View.GONE) {
                binding.masterCard.visibility = View.VISIBLE
                if (binding.mpesa.visibility == View.VISIBLE) {
                    binding.mpesa.visibility = View.GONE
                }
                if (binding.visa.visibility == View.VISIBLE) {
                    binding.visa.visibility = View.GONE
                }
            }
//            else if (binding.masterCard.visibility == View.VISIBLE) {
//                binding.masterCard.visibility = View.GONE
//            }
        }
    }

    private fun restartApp() {
        val i = Intent(applicationContext, PaymentActivity::class.java)
        startActivity(i)
        finish()
    }
}