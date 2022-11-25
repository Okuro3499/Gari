package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityPaymentBinding
import com.justin.gari.models.bookingCarModels.BookCar
import com.justin.gari.models.bookingCarModels.BookCarResponse
import com.justin.gari.utils.SettingsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class PaymentActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var settingsManager: SettingsManager
    var carId: String? = null
    var userId: String? = null
    var carName: String? = null
    var drive: String? = null
    var bookDateFrom: String? = null
    var bookDateTo: String? = null
    var destination: String? = null
    var totalDays: String? = null
    var totalAmount: String? = null
    var firstNameHeader: String? = null
    var lastNameHeader : String? = null
    var amntPerDay : String? = null

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
        firstNameHeader = sharedPreferences.getString("first_name", "")
        lastNameHeader = sharedPreferences.getString("last_name", "")
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

        carId = intent.getStringExtra("car_id")
        userId = sharedPreferences.getString("user_id", "")
        carName = intent.getStringExtra("car_name")
        drive = intent.getStringExtra("drive")
        bookDateFrom = intent.getStringExtra("book_date_from")
        bookDateTo  = intent.getStringExtra("book_date_to")
        destination = intent.getStringExtra("destination")
        totalDays   = intent.getStringExtra("total_days")
        totalAmount = intent.getStringExtra("total_amount")
        amntPerDay = intent.getStringExtra("amntPerDay")

        binding.name.text = "$firstNameHeader $lastNameHeader"
        binding.carName.text = carName
        binding.drive.text = drive
        binding.dateFrom.text = bookDateFrom
        binding.dateTo.text = bookDateTo
        binding.days.text= totalDays
        binding.amountPerDay.text = amntPerDay
        binding.destination.text= destination
        binding.totalAmount.text = totalAmount


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
            val intent = Intent(this@PaymentActivity, DetailActivity::class.java)
            startActivity(intent)
        }

        binding.mpesaButton.setOnClickListener {
            book()
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

        binding.pay1.setOnClickListener{
            book()
        }

//        binding.paypalButton.setOnClickListener {
//            if (binding.mpesa.visibility == View.GONE) {
//                binding.mpesa.visibility = View.VISIBLE
//            } else if (binding.mpesa.visibility == View.VISIBLE) {
//                binding.mpesa.visibility = View.GONE
//            }
//        }
//
//        binding.pay1.setOnClickListener{
//
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

        binding.pay3.setOnClickListener{
            book()
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

        binding.pay4.setOnClickListener{
            book()
        }
    }

    private fun book() {
        apiClient = ApiClient
        // display a progress dialog
        val progressDialog = ProgressDialog(this@PaymentActivity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Booking..") // set message
        progressDialog.show()

        val today: LocalDate = LocalDate.now()
        val bookingInfo = BookCar(
            carId?.toInt(),
            userId?.toInt(),
            bookDateFrom,
            bookDateTo,
            destination,
            drive,
            totalDays?.toInt(),
            totalAmount?.toInt(),
            carName,
            "$firstNameHeader $lastNameHeader",
            "$firstNameHeader $lastNameHeader",
            today.toString(),
            bookDateFrom,
            bookDateTo
        )

        Log.e("Gideon", "onSuccess: $bookingInfo")
        apiClient.getApiService(this).bookingCar(bookingInfo).enqueue(object : Callback<BookCarResponse> {
            override fun onResponse(call: Call<BookCarResponse>, response: Response<BookCarResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
//                    Snackbar.make(it, "Booked Successfully", Snackbar.LENGTH_SHORT).show()
                    Log.e("Gideon", "onSuccess: ${response.body()}")

                }
            }

            override fun onFailure(call: Call<BookCarResponse>, t: Throwable) {
                progressDialog.dismiss()
//                Snackbar.make(it, "${t.message}", Snackbar.LENGTH_SHORT).show()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun restartApp() {
        recreate()
    }
}