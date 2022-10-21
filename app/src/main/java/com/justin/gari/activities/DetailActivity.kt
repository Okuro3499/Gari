package com.justin.gari.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.adapters.SliderPageAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityDetailBinding
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityDetailBinding
    private val myCalendarFrom: Calendar = Calendar.getInstance()
    private val myCalendarTo: Calendar = Calendar.getInstance()
    var selectedDrive: String? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        }
        else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        //binding.shimmerLayout.startShimmer();

        binding.swipeRefresh.setOnRefreshListener {
            //binding.shimmerLayout.startShimmer();
            getAllData()
        }

        //open date from dialog
        val date1 = OnDateSetListener { view, yearFrom, monthFrom, dayFrom ->
            myCalendarFrom[Calendar.YEAR] = yearFrom
            myCalendarFrom[Calendar.MONTH] = monthFrom
            myCalendarFrom[Calendar.DAY_OF_MONTH] = dayFrom
            updateLabelFrom()
        }
        binding.ETDFrom.setOnClickListener {
            DatePickerDialog(
                this@DetailActivity,
                date1,
                myCalendarFrom[Calendar.YEAR],
                myCalendarFrom[Calendar.MONTH],
                myCalendarFrom[Calendar.DAY_OF_MONTH]
            ).show()
        }

        //open date to dialog
        val date2 = OnDateSetListener { view, yearTo, monthTo, dayTo ->
            myCalendarTo[Calendar.YEAR] = yearTo
            myCalendarTo[Calendar.MONTH] = monthTo
            myCalendarTo[Calendar.DAY_OF_MONTH] = dayTo
            updateLabelTo()
        }
        binding.ETDTo.setOnClickListener {
            DatePickerDialog(
                this@DetailActivity,
                date2,
                myCalendarTo[Calendar.YEAR],
                myCalendarTo[Calendar.MONTH],
                myCalendarTo[Calendar.DAY_OF_MONTH]
            ).show()
        }

        //get car details
        //receiving intents
        val carId = intent.getStringExtra("car_id")
        getCarDetails()

        binding.nav.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.back.setOnClickListener{
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        //save car for future bookings
        binding.ibSave.setOnClickListener {
            val car_id = carId
            val client_id = sharedPreferences.getString("client_id", "default")
            val saveInfo = SaveCar(car_id, client_id)

            apiClient.getApiService(this).saveCar(saveInfo).enqueue(object : Callback<SaveCarResponse> {
                    override fun onResponse(call: Call<SaveCarResponse>, response: Response<SaveCarResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@DetailActivity, "Saved Successfully", Toast.LENGTH_LONG).show()
                            Log.e("Gideon", "onSuccess: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<SaveCarResponse>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_LONG).show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }

        //make car booking
//        binding.btBook.setOnClickListener {

//            TODO: migrate booking actions
            //selected radio button
//            if (binding.radioSelfDrive.isChecked) {
//                selectedDrive = binding.radioSelfDrive.text.toString()
//            }
//            else if (binding.radioChauffeured.isChecked) {
//                selectedDrive = binding.radioChauffeured.text.toString()
//            }
//
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
//            binding.ETDFrom.setText(dateFormat.format(myCalendarFrom.time))
//            binding.ETDTo.setText(dateFormat.format(myCalendarTo.time))
//
//            val car_id = carId
//            val client_id = sharedPreferences.getString("client_id", "default")
//            val book_date_from = binding.ETDFrom!!.text.toString().trim()
//            val book_date_to = binding.ETDTo!!.text.toString().trim()
//            val destination = binding.etDestination.text.toString().trim()
//            val drive = selectedDrive
//            val total_days = tvTotalDays.text.toString().trim()
//            val total_amount = tvTotalAmount.text.toString().trim()
//            val bookingInfo = BookCar(car_id, client_id, book_date_from, book_date_to, destination, drive, total_days, total_amount)
//
//            apiClient.getApiService(this).bookingCar(bookingInfo).enqueue(object : Callback<BookCarResponse> {
//                    override fun onResponse(call: Call<BookCarResponse>, response: Response<BookCarResponse>) {
//                        if (response.isSuccessful) {
//                            Toast.makeText(this@DetailActivity, "Booked Successfully", Toast.LENGTH_LONG).show()
//                            Log.e("Gideon", "onSuccess: ${response.body()!!.book_car}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<BookCarResponse>, t: Throwable) {
//                        Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_LONG).show()
//                        Log.e("Gideon", "onFailure: ${t.message}")
//                    }
//                })

//            val intent = Intent(this, PaymentActivity::class.java)
//            startActivity(intent)
//        }

//        val client_id = sharedPreferences.getString("client_id", "default")
//        val editor: SharedPreferences.Editor = sharedPreferences.edit()
//        apiClient.getApiService(this).getUserImageInfo(client_id).enqueue(object : Callback<SingleClientImageInfoResponse> {
//                override fun onResponse(call: Call<SingleClientImageInfoResponse>, response: Response<SingleClientImageInfoResponse>) {
//                    if (response.isSuccessful) {
//                        //fetching images to
//                        val userProfile = response.body()!!.single_clientInfo.user_photo_url.toString().trim()
//                        editor.putString("userPhoto", userProfile)
//                        editor.apply()
//                    }
//                }
//
//                override fun onFailure(call: Call<SingleClientImageInfoResponse>, t: Throwable) {
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

        if (settingsManager.loadNightModeState()) {
            header.themeSwitch!!.isChecked = true
        }
        header.themeSwitch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            }
            else {
                settingsManager.setNightModeState(false)
                restartApp()
            }
        }

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            //TODO: set visibility
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@DetailActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this@DetailActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles = Intent(this@DetailActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@DetailActivity, LoginActivity::class.java)
                    startActivity(intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@DetailActivity, AboutActivity::class.java)
                    startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@DetailActivity, LoginActivity::class.java)
                    startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })
    }

    private fun getCarDetails() {
        // display a progress dialog
        val progressDialog = ProgressDialog(this@DetailActivity)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Fetching Details..") // set message
        progressDialog.show()

        apiClient = ApiClient
        val carId = intent.getStringExtra("car_id")
        apiClient.getApiService(this).getCarDetails(carId).enqueue(object : Callback<SingleCarModel> {
            override fun onResponse(call: Call<SingleCarModel>, response: Response<SingleCarModel>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    //binding.shimmerLayout.stopShimmer();
                    //binding.shimmerLayout.visibility = View.GONE;
                    binding.cons.visibility = View.VISIBLE;

                    //fetching images to slider
                    val imageUrls = arrayOf(
                        response.body()!!.single_car.front_view.toString(),
                        response.body()!!.single_car.back_view.toString(),
                        response.body()!!.single_car.right_view.toString(),
                        response.body()!!.single_car.left_view.toString(),
                        response.body()!!.single_car.interior_1.toString(),
                        response.body()!!.single_car.interior_2.toString()
                    )

                    val adapter = SliderPageAdapter(this@DetailActivity, imageUrls)
                    binding.viewPager.adapter = adapter

                    binding.tvCarName.text =response.body()!!.single_car.car_name.toString()
                    binding.tvStatus.text = response.body()!!.single_car.status.toString()
                    binding.tvTransmission.text = response.body()!!.single_car.transmission.toString()
                    binding.tvEngine.text = response.body()!!.single_car.engine.toString()
                    binding.tvColor.text = response.body()!!.single_car.color.toString()
                    binding.tvRegistration.text = response.body()!!.single_car.registration.toString()
                    binding.tvPassengers.text = response.body()!!.single_car.passengers.toString()
                    binding.tvCompany.text = response.body()!!.single_car.company.toString()
                    binding.tvPrice.text = response.body()!!.single_car.price.toString()
                    binding.tvDoors.text = response.body()!!.single_car.doors.toString()
                    binding.tvDriveOption.text = response.body()!!.single_car.drive.toString()
                    binding.tvFeature1.text = response.body()!!.single_car.feature_1.toString()
                    binding.tvFeature2.text = response.body()!!.single_car.feature_2.toString()
                    binding.tvFeature3.text = response.body()!!.single_car.feature_3.toString()
                    binding.tvFeature4.text = response.body()!!.single_car.feature_4.toString()
                    binding.tvFeature5.text = response.body()!!.single_car.feature_5.toString()

                    Log.e("Gideon", "onSuccess: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<SingleCarModel>, t: Throwable) {
                progressDialog.dismiss()
                //binding.shimmerLayout.stopShimmer();
                //binding.shimmerLayout.visibility = View.GONE;
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })

    }

    private fun restartApp() {
        recreate()
//        val i = Intent(applicationContext, DetailActivity::class.java)
//        startActivity(i)
//        finish()
    }

    private fun getAllData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false

            getCarDetails()
        }
    }

    //get date from
    private fun updateLabelFrom() {
        val myFormatFrom = "dd/MM/yyyy"
        val dateFormatFrom = SimpleDateFormat(myFormatFrom, Locale.US)
        binding.ETDFrom.text = dateFormatFrom.format(myCalendarFrom.time)
    }

    //get date to
    private fun updateLabelTo() {
        val myFormatTo = "dd/MM/yyyy"
        val dateFormatTo = SimpleDateFormat(myFormatTo, Locale.US)
        binding.ETDTo.text = dateFormatTo.format(myCalendarTo.time)
        differenceBetweenDays()
    }

    //get the difference between to and from
    private fun differenceBetweenDays() {
        val myFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val toDate = binding.ETDTo.text.toString()
        val fromDate = binding.ETDFrom.text.toString()

        val fromDate1 = myFormat.parse(fromDate)
        val toDate2 = myFormat.parse(toDate)
        val diff = toDate2!!.time - fromDate1!!.time
        val days = (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString()
        binding.tvTotalDays.text = days

        multiplyPriceAndDays()
    }

    //multiply Price And Days
    private fun multiplyPriceAndDays() {
        val price = tvPrice.text.toString()
        val noOfDays = tvTotalDays.text.toString()
        val finalPrice = price.toInt()
        val finalNoOfDays = noOfDays.toInt()

        val total = StringBuilder().apply {
            append(finalPrice * finalNoOfDays)
        }
        binding.tvTotalAmount.text = "Ksh. $total"
    }

    //items selected from navigation view
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@DetailActivity, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}
