package com.justin.gari.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityPaymentBinding
import com.justin.gari.models.bookingCarModels.BookCar
import com.justin.gari.models.bookingCarModels.BookCarResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UseSwitchCompatOrMaterialCode")
class PaymentActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityPaymentBinding
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
    var lastNameHeader: String? = null
    var amntPerDay: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref!!.loadNightModeState()}")
        if (pref!!.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val profileHeader = pref!!.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val lastNameTextView = header.findViewById<TextView>(R.id.lastName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<Switch>(R.id.themeSwitch)
        firstNameTextView.text = "${pref!!.getFIRSTNAME()}"
        val firstName = firstNameTextView.text

        emailTextView.text = pref!!.getEMAIL()

        firstNameHeader = pref!!.getFIRSTNAME()
        lastNameHeader = pref!!.getLASTNAME()

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

        carId = intent.getStringExtra("car_id")
        userId = pref!!.getUSERID()
        carName = intent.getStringExtra("car_name")
        drive = intent.getStringExtra("drive")
        bookDateFrom = intent.getStringExtra("book_date_from")
        bookDateTo = intent.getStringExtra("book_date_to")
        destination = intent.getStringExtra("destination")
        totalDays = intent.getStringExtra("total_days")
        totalAmount = intent.getStringExtra("total_amount")
        amntPerDay = intent.getStringExtra("amntPerDay")

        binding.name.text = getString(R.string.full_name_, firstNameHeader, lastNameHeader)
        binding.carName.text = carName
        binding.drive.text = drive
        binding.dateFrom.text = intent.getStringExtra("book_date_from")
        binding.dateTo.text = intent.getStringExtra("book_date_to")
        binding.days.text = totalDays
        binding.amountPerDay.text = amntPerDay
        binding.destination.text = destination
        binding.totalAmount.text = totalAmount

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
            val intent = Intent(this, DetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
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

        binding.pay1.setOnClickListener {
            if (TextUtils.isEmpty(binding.phoneNumber.text.toString().trim())) {
                binding.phoneNumber.error = "Kindly enter a phone number to pay!"
            } else {
                book()
            }

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

        binding.pay3.setOnClickListener {
            if (TextUtils.isEmpty(binding.visaCardNumber.text.toString().trim())) {
                binding.visaCardNumber.error = "Kindly enter a card number to pay!"
            } else if (TextUtils.isEmpty(binding.validity.text.toString().trim())) {
                binding.validity.error = "Kindly enter card expiry date!"
            } else if (TextUtils.isEmpty(binding.cvc.text.toString().trim())) {
                binding.cvc.error = "Kindly enter CVV!"
            } else if (TextUtils.isEmpty(binding.cardHolderName.text.toString().trim())) {
                binding.cardHolderName.error = "Kindly enter card holder name!"
            } else {
                book()
            }
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

        binding.pay4.setOnClickListener {
            if (TextUtils.isEmpty(binding.visaCardNumber.text.toString().trim())) {
                binding.visaCardNumber.error = "Kindly enter a card number to pay!"
            } else if (TextUtils.isEmpty(binding.validity.text.toString().trim())) {
                binding.validity.error = "Kindly enter card expiry date!"
            } else if (TextUtils.isEmpty(binding.cvc.text.toString().trim())) {
                binding.cvc.error = "Kindly enter CVV!"
            } else if (TextUtils.isEmpty(binding.cardHolderName.text.toString().trim())) {
                binding.cardHolderName.error = "Kindly enter card holder name!"
            } else {
                book()
            }
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.refresh.setOnClickListener {
            restartApp()
        }
    }

    private fun book() {
        // display a progress dialog
        val progressDialog = ProgressDialog(this@PaymentActivity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Booking..") // set message
        progressDialog.show()

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateFrom = LocalDateTime.parse("$bookDateFrom 00:00", formatter)
        val dateTo = LocalDateTime.parse("$bookDateTo 00:00", formatter)

        val formattedDateFrom: String = formatter.format(dateFrom)
        val formattedDateTo: String = formatter.format(dateTo)

        val zoneNow: ZonedDateTime = ZonedDateTime.now(ZoneId.of("GMT+03:00"))
        val dateTimeNow: LocalDateTime = zoneNow.toLocalDateTime()

        val bookingInfo = BookCar(
            carId?.toInt(), userId?.toInt(), formattedDateFrom, formattedDateTo, destination,
            drive, totalDays?.toInt(), totalAmount?.toInt(), carName,
            "$firstNameHeader $lastNameHeader",
            "$firstNameHeader $lastNameHeader",
            dateTimeNow.toString(),
            formattedDateFrom,
            formattedDateTo,
        )

        Log.e("Gideon", "onSuccess: $bookingInfo")
        apiClient.getApiService().bookingCar(bookingInfo).enqueue(object : Callback<BookCarResponse> {
            override fun onResponse(call: Call<BookCarResponse>, response: Response<BookCarResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    binding.successPage.visibility = View.VISIBLE
                    binding.paymentPage.visibility = View.GONE
//                  Snackbar.make(it, "Booked Successfully", Snackbar.LENGTH_SHORT).show()
                    Log.e("Gideon", "onSuccess: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<BookCarResponse>, t: Throwable) {
                progressDialog.dismiss()
                binding.errorPage.visibility = View.VISIBLE
                binding.paymentPage.visibility = View.GONE
                binding.message.text = t.message
//              Snackbar.make(it, "${t.message}", Snackbar.LENGTH_SHORT).show()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }

    private fun restartApp() {
        finish()
        startActivity(intent)
    }
}