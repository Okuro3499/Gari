package com.justin.gari.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.adapters.SliderPageAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityDetailBinding
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import com.justin.gari.utils.SettingsManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.nav_header.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityDetailBinding
    private val myCalendarFrom: Calendar = Calendar.getInstance()
    private val myCalendarTo: Calendar = Calendar.getInstance()
    var selectedDrive: String? = null
    private lateinit var settingsManager: SettingsManager
    var total : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

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

//        binding.swipeRefresh.setOnRefreshListener {
//            getAllData()
//        }

        //open date from dialog
        val date1 = OnDateSetListener { _, yearFrom, monthFrom, dayFrom ->
            myCalendarFrom[Calendar.YEAR] = yearFrom
            myCalendarFrom[Calendar.MONTH] = monthFrom
            myCalendarFrom[Calendar.DAY_OF_MONTH] = dayFrom

            updateLabelFrom()
        }

//         datePicker = new DatePickerDialog(this)
//        date1.setMinDate(System.currentTimeMillis() - 1000)
//        DatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
        binding.ETDFrom.setOnClickListener {

           val datees = DatePickerDialog(
                this@DetailActivity,
                date1,
                myCalendarFrom[Calendar.YEAR],
                myCalendarFrom[Calendar.MONTH],
                myCalendarFrom[Calendar.DAY_OF_MONTH]
            )
            datees.datePicker.minDate = System.currentTimeMillis() - 1000

            datees.show()

        }

        //open date to dialog
        val date2 = OnDateSetListener { _, yearTo, monthTo, dayTo ->
            myCalendarTo[Calendar.YEAR] = yearTo
            myCalendarTo[Calendar.MONTH] = monthTo
            myCalendarTo[Calendar.DAY_OF_MONTH] = dayTo
            updateLabelTo()
        }
        binding.ETDTo.setOnClickListener {
            val datees2 =DatePickerDialog(
                this@DetailActivity,
                date2,
                myCalendarTo[Calendar.YEAR],
                myCalendarTo[Calendar.MONTH],
                myCalendarTo[Calendar.DAY_OF_MONTH]
            )
            datees2.datePicker.minDate = System.currentTimeMillis() - 1000
            datees2.show()
        }

        //get car details
        //receiving intents
        val carId = intent.getStringExtra("car_id")
        getCarDetails()

        binding.nav.setOnClickListener {
            if (firstNameHeader != "") {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        //save car for future bookings
        binding.ibSave.setOnClickListener {
            val today: LocalDate = LocalDate.now()
            val car_id = carId
            val user_id = sharedPreferences.getString("user_id", "")
            val saveInfo = SaveCar(car_id, user_id, today.toString())

            apiClient.getApiService(this).saveCar(saveInfo).enqueue(object : Callback<SaveCarResponse> {
                    override fun onResponse(call: Call<SaveCarResponse>,
                        response: Response<SaveCarResponse>) {
                        if (response.isSuccessful) {
                            Snackbar.make(it, "Saved Successfully", Snackbar.LENGTH_SHORT).show()
                            Log.e("Gideon", "onSuccess: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<SaveCarResponse>, t: Throwable) {
                        Snackbar.make(it, "Failed to save kindly retry", Snackbar.LENGTH_SHORT).show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }

        //TODO: migrate booking actions
        //make car booking
        binding.btBook.setOnClickListener {
            if (binding.ETDFrom.text.toString().trim() == "") {
                binding.ETDFrom.error = "Kindly choose from date"
            } else if (binding.ETDTo.text.toString().trim() == "") {
                binding.ETDTo.error = "Kindly choose to date"
            } else if (binding.radioDriveGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this@DetailActivity, "Kindly choose drive mode", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(binding.etDestination.text.toString().trim())) {
                binding.etDestination.error = "Kindly enter a destination!"
            } else {
                //selected radio button
                if (binding.radioSelfDrive.isChecked) {
                    selectedDrive = binding.radioSelfDrive.text.toString()
                } else if (binding.radioChauffeured.isChecked) {
                    selectedDrive = binding.radioChauffeured.text.toString()
                }


                if (firstNameHeader != "") {
                    val intent = Intent(this, PaymentActivity::class.java)
                    val car_id = carId
                    val book_date_from = binding.ETDFrom.text.toString().trim()
                    val book_date_to = binding.ETDTo.text.toString().trim()
                    val destination = binding.etDestination.text.toString().trim()
                    val drive = selectedDrive
                    val total_days = tvTotalDays.text.toString().trim()
                    val total_amount = total
                    intent.putExtra("car_name", binding.tvCarName.text.toString())
                    intent.putExtra("drive", drive.toString())
                    intent.putExtra("car_id", car_id.toString())
                    intent.putExtra("book_date_from", book_date_from)
                    intent.putExtra("book_date_to", book_date_to)
                    intent.putExtra("destination", destination)
                    intent.putExtra("total_days",total_days)
                    intent.putExtra("total_amount",total_amount)
                    intent.putExtra("amntPerDay", binding.tvPrice.text.toString())

                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                }
            }
        }

        if (firstNameHeader != "") {
            binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.END
            )
            binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
                when (item.itemId) {
                    R.id.home -> {
                        startActivity(
                            Intent(
                                this@DetailActivity,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.profile -> {
                        val intentProfile =
                            Intent(this@DetailActivity, UserProfileActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.myVehicles -> {
                        val intentMyVehicles =
                            Intent(this@DetailActivity, VehiclesActivity::class.java)
                        startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.logout -> {
                        editor.clear()
                        editor.apply()
                        val intentLogout = Intent(this@DetailActivity, MainActivity::class.java)
                        startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                        finish()
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
        else {
            binding.drawerLayout.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                GravityCompat.START
            )
            binding.outNavView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)

                when (item.itemId) {
                    R.id.login -> {
                        startActivity(
                            Intent(
                                this@DetailActivity,
                                LoginActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.createAccount -> {
                        val intentProfile =
                            Intent(this@DetailActivity, RegisterActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
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
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        }
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

                        binding.tvCarName.text = response.body()!!.single_car.car_name.toString()
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
                    binding.errorPage.visibility = View.VISIBLE
                    binding.message.text = t.message
//                    binding.swipeRefresh.visibility = View.GONE
                    //binding.shimmerLayout.stopShimmer();
                    //binding.shimmerLayout.visibility = View.GONE;
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
    }

    private fun restartApp() {
        recreate()
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

        total = StringBuilder().apply {
            append(finalPrice * finalNoOfDays)
        }.toString()
        binding.tvTotalAmount.text = "Ksh. $total"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@DetailActivity, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}
