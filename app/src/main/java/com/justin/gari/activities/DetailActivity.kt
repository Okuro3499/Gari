package com.justin.gari.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.adapters.SliderPageAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.models.bookingCarModels.BookCar
import com.justin.gari.models.bookingCarModels.BookCarResponse
import com.justin.gari.models.carModels.SingleCarModel
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import kotlinx.android.synthetic.main.activity_detail.*
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
    private val myCalendarFrom: Calendar = Calendar.getInstance()
    private val myCalendarTo: Calendar = Calendar.getInstance()
    var dateFrom: EditText? = null
    var dateTo: EditText? = null
    var selfDrive: RadioButton? = null
    var chauffeured: RadioButton? = null
    var selectedDrive: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val carNameTextView = findViewById<TextView>(R.id.tvCarName)
        val driveOptionTextView = findViewById<TextView>(R.id.tvDriveOption)
        val transmissionTextView = findViewById<TextView>(R.id.tvTransmission)
        val priceTextView = findViewById<TextView>(R.id.tvPrice)
        val engineTextView = findViewById<TextView>(R.id.tvEngine)
        val colorTextView = findViewById<TextView>(R.id.tvColor)
        val registrationTextView = findViewById<TextView>(R.id.tvRegistration)
        val passengersTextView = findViewById<TextView>(R.id.tvPassengers)
        val companyTextView = findViewById<TextView>(R.id.tvCompany)
        val doorsTextView = findViewById<TextView>(R.id.tvDoors)
        val statusTextView = findViewById<TextView>(R.id.tvStatus)
        val feature1TextView = findViewById<TextView>(R.id.tvFeature1)
        val feature2TextView = findViewById<TextView>(R.id.tvFeature2)
        val feature3TextView = findViewById<TextView>(R.id.tvFeature3)
        val feature4TextView = findViewById<TextView>(R.id.tvFeature4)
        val feature5TextView = findViewById<TextView>(R.id.tvFeature5)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        dateFrom = findViewById<View>(R.id.ETDFrom) as EditText
        dateTo = findViewById<View>(R.id.ETDTo) as EditText
        selfDrive = findViewById<View>(R.id.radioSelfDrive) as RadioButton
        chauffeured = findViewById<View>(R.id.radioChauffeured) as RadioButton
        val viewPager = findViewById<ViewPager>(R.id.view_pager)

        //open date from dialog
        val date1 = OnDateSetListener { view, yearFrom, monthFrom, dayFrom ->
            myCalendarFrom[Calendar.YEAR] = yearFrom
            myCalendarFrom[Calendar.MONTH] = monthFrom
            myCalendarFrom[Calendar.DAY_OF_MONTH] = dayFrom
            updateLabelFrom()
        }
        dateFrom!!.setOnClickListener {
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
        dateTo!!.setOnClickListener {
            DatePickerDialog(
                this@DetailActivity,
                date2,
                myCalendarTo[Calendar.YEAR],
                myCalendarTo[Calendar.MONTH],
                myCalendarTo[Calendar.DAY_OF_MONTH]
            ).show()
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get car details
        //receiving intents
        val carId = intent.getStringExtra("car_id")
        apiClient = ApiClient
        apiClient.getApiService(this).getCarDetails(carId)
            .enqueue(object : Callback<SingleCarModel> {
                override fun onResponse(
                    call: Call<SingleCarModel>,
                    response: Response<SingleCarModel>
                ) {
                    if (response.isSuccessful) {
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
                        viewPager.adapter = adapter

                        carNameTextView.text = response.body()!!.single_car.car_name.toString()
                        statusTextView.text = response.body()!!.single_car.status.toString()
                        transmissionTextView.text =
                            response.body()!!.single_car.transmission.toString()
                        engineTextView.text = response.body()!!.single_car.engine.toString()
                        colorTextView.text = response.body()!!.single_car.color.toString()
                        registrationTextView.text =
                            response.body()!!.single_car.registration.toString()
                        passengersTextView.text = response.body()!!.single_car.passengers.toString()
                        companyTextView.text = response.body()!!.single_car.company.toString()
                        priceTextView.text = response.body()!!.single_car.price.toString()
                        doorsTextView.text = response.body()!!.single_car.doors.toString()
                        driveOptionTextView.text = response.body()!!.single_car.drive.toString()
                        feature1TextView.text = response.body()!!.single_car.feature_1.toString()
                        feature2TextView.text = response.body()!!.single_car.feature_2.toString()
                        feature3TextView.text = response.body()!!.single_car.feature_3.toString()
                        feature4TextView.text = response.body()!!.single_car.feature_4.toString()
                        feature5TextView.text = response.body()!!.single_car.feature_5.toString()
                    }
                }

                override fun onFailure(call: Call<SingleCarModel>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        //save car for future bookings
        val saveBt = findViewById<ImageButton>(R.id.ibSave)
        saveBt.setOnClickListener {
            val car_id = carId
            val client_id = sharedPreferences.getString("client_id", "default")
            val saveInfo = SaveCar(car_id, client_id)

            apiClient.getApiService(this).saveCar(saveInfo)
                .enqueue(object : Callback<SaveCarResponse> {
                    override fun onResponse(
                        call: Call<SaveCarResponse>,
                        response: Response<SaveCarResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@DetailActivity,
                                "Saved Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("Gideon", "onSuccess: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<SaveCarResponse>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_LONG)
                            .show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
        }

        //make car booking
        val bookBt = findViewById<Button>(R.id.btBook)
        bookBt.setOnClickListener {
            //selected radio button
            if (selfDrive!!.isChecked) {
                selectedDrive = selfDrive!!.text.toString()
            } else if (chauffeured!!.isChecked) {
                selectedDrive = chauffeured!!.text.toString()
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFrom?.setText(dateFormat.format(myCalendarFrom.time))
            dateTo?.setText(dateFormat.format(myCalendarTo.time))

            val car_id = carId
            val client_id = sharedPreferences.getString("client_id", "default")
            val book_date_from = dateFrom!!.text.toString().trim()
            val book_date_to = dateTo!!.text.toString().trim()
            val destination = findViewById<EditText>(R.id.etDestination).text.toString().trim()
            val drive = selectedDrive
            val total_days = tvTotalDays.text.toString().trim()
            val total_amount = tvTotalAmount.text.toString().trim()
            val bookingInfo = BookCar(
                car_id,
                client_id,
                book_date_from,
                book_date_to,
                destination,
                drive,
                total_days,
                total_amount
            )

            apiClient.getApiService(this).bookingCar(bookingInfo)
                .enqueue(object : Callback<BookCarResponse> {
                    override fun onResponse(
                        call: Call<BookCarResponse>,
                        response: Response<BookCarResponse>
                    ) {

                        if (response.isSuccessful) {

//                            apiClient.getApiService(this@DetailActivity).changeStatus(carId)

                            Toast.makeText(
                                this@DetailActivity,
                                "Booked Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("Gideon", "onSuccess: ${response.body()!!.book_car}")
                        }
                    }

                    override fun onFailure(call: Call<BookCarResponse>, t: Throwable) {
                        Toast.makeText(this@DetailActivity, "${t.message}", Toast.LENGTH_LONG)
                            .show()
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })

//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
        }

        //open side navigation view
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT)
                    .show()
                R.id.profile -> Toast.makeText(
                    applicationContext,
                    "Clicked Profile",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.myVehicles -> Toast.makeText(
                    applicationContext,
                    "Clicked My Vehicles",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.logout -> Toast.makeText(
                    applicationContext,
                    "Clicked Logout",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.about -> Toast.makeText(
                    applicationContext,
                    "Clicked About",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.help -> Toast.makeText(
                    applicationContext,
                    "Clicked Help",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            true
        }
    }

    //get date from
    fun updateLabelFrom() {
        val myFormatFrom = "dd/MM/yyyy"
        val dateFormatFrom = SimpleDateFormat(myFormatFrom, Locale.US)
        dateFrom?.setText(dateFormatFrom.format(myCalendarFrom.time))
    }

    //get date to
    fun updateLabelTo() {
        val myFormatTo = "dd/MM/yyyy"
        val dateFormatTo = SimpleDateFormat(myFormatTo, Locale.US)
        dateTo?.setText(dateFormatTo.format(myCalendarTo.time))
        differenceBetweenDays()

    }

    //get the difference between to and from
    fun differenceBetweenDays() {
        val totalDays = findViewById<TextView>(R.id.tvTotalDays)

        val myFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val toDate = dateTo!!.text.toString()
        val fromDate = dateFrom!!.text.toString()

        val fromDate1 = myFormat.parse(fromDate)
        val toDate2 = myFormat.parse(toDate)
        val diff = toDate2!!.time - fromDate1!!.time
        val days = (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toString()
        totalDays.text = days

        multiplyPriceAndDays()
    }

    fun multiplyPriceAndDays() {
        val totalDays = findViewById<TextView>(R.id.tvTotalAmount)
        val price = tvPrice.text.toString()
        val noOfDays = tvTotalDays.text.toString()
        val finalPrice = price.toInt()
        val finalNoOfDays = noOfDays.toInt()

        var total = StringBuilder().apply {
            append(finalPrice * finalNoOfDays)
        }
        totalDays.text = total
    }

    //items selected from navigation view
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}

//class MainActivity : AppCompatActivity() {
//    private val imageUrls = arrayOf(
//        "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
//        "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
//        "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
//        "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
//        "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val viewPager = findViewById<ViewPager>(R.id.view_pager)
//        val adapter = ViewPagerAdapter(this, imageUrls)
//        viewPager.adapter = adapter
//    }
//}
