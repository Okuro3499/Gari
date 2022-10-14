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
import com.justin.gari.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityPaymentBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*

class PaymentActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    lateinit var toggle: ActionBarDrawerToggle

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState() == true) {
            setTheme(R.style.DarkGari)
        } else
            setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiClient = ApiClient

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
//        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                    startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@PaymentActivity, ProfileCompleteActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles =
//                        Intent(this@PaymentActivity, VehiclesActivity::class.java)
//                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@PaymentActivity, LoginActivity::class.java)
                    startActivity(intentLogin)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@PaymentActivity, AboutActivity::class.java)
                    startActivity(intentAbout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@PaymentActivity, LoginActivity::class.java)
                    startActivity(intentHelp)
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

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