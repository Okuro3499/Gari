package com.justin.gari.activities

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.adapters.SliderPageAdapter
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityDetailBinding
import com.justin.gari.models.carModels.Car
import com.justin.gari.models.saveCarModels.SaveCar
import com.justin.gari.models.saveCarModels.SaveCarResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class DetailActivity : AppCompatActivity() {
    lateinit var pref: SharedPrefManager
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityDetailBinding
    private val myCalendarFrom: Calendar = Calendar.getInstance()
    private val myCalendarTo: Calendar = Calendar.getInstance()
    private var selectedDrive: String? = null
    private var total: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref.loadNightModeState()}")
        if (pref.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        // Obtain the textColor attribute from the current theme
        val typedArray = theme.obtainStyledAttributes(R.styleable.Gari)
        val textColor = typedArray.getColor(R.styleable.Gari_textColor, Color.BLACK)
        typedArray.recycle() // Always recycle TypedArray

        val car: Car? = intent.getParcelableExtra("car")
        Log.d("ollond", "Car: $car")
        car?.car_images?.let { imageUrls ->
            if (imageUrls.isNotEmpty()) {
                val adapter = SliderPageAdapter(this@DetailActivity, imageUrls.toTypedArray())
                binding.viewPager.adapter = adapter
            }
        }
        binding.tvCarName.text = car?.car_name
        binding.tvStatus.text = car?.status
        binding.tvTransmission.text = car?.transmission
        binding.tvEngine.text = car?.cc
        binding.tvColor.text = car?.color
        binding.tvRegistration.text = car?.registration
        binding.tvPassengers.text = "${car?.passengers}"
        binding.tvCompany.text = "${car?.company_id}"
        binding.tvPrice.text = car?.price
        binding.tvDoors.text = car?.doors
        binding.tvDriveOption.text = car?.drive?.joinToString(", ") ?: "N/A"
        binding.tvFuel.text = car?.fuel
        car?.features?.forEach { feature ->
            val textView = TextView(this).apply {
                text = feature
                layoutParams = GridLayout.LayoutParams(GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)).apply {
                    width = 0
                    bottomMargin = resources.getDimensionPixelSize(R.dimen._2dp)
                    topMargin = resources.getDimensionPixelSize(R.dimen._2dp)
                    leftMargin = resources.getDimensionPixelSize(R.dimen._4dp)
                    rightMargin = resources.getDimensionPixelSize(R.dimen._4dp)
                }
                textSize = 14f
                typeface = ResourcesCompat.getFont(context, R.font.poppins_regular)
                setTextColor(textColor)
            }
            binding.gridFeatures.addView(textView)
        }

        val profileHeader = pref.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val outHeader = binding.outNavView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<SwitchCompat>(R.id.themeSwitch)
        val outSwitch = outHeader.findViewById<SwitchCompat>(R.id.themeSwitch)
        firstNameTextView.text = "${pref.getFIRSTNAME()}"
        val firstName = firstNameTextView.text
        emailTextView.text = pref.getEMAIL()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        if (pref.loadNightModeState()) {
            inSwitch.isChecked = true
            outSwitch.isChecked = true
            inSwitch.text = getString(R.string.light_mode)
            outSwitch.text = getString(R.string.light_mode)
        } else{
            inSwitch.text = getString(R.string.dark_mode)
            outSwitch.text = getString(R.string.dark_mode)
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

        outHeader.findViewById<SwitchCompat>(R.id.themeSwitch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.setNightModeState(true)
                pref.setSWITCHEDTHEME(true)
                outSwitch.text = getString(R.string.dark_mode)
                restartApp()
            } else {
                pref.setNightModeState(false)
                pref.setSWITCHEDTHEME(false)
                outSwitch.text = getString(R.string.dark_mode)
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

        binding.ETDFrom.setOnClickListener {
            if (binding.ETDTo.text.isEmpty()) {
                binding.ETDTo.text = ""
            }
            val datees = DatePickerDialog(
                this@DetailActivity, date1,
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
            if (binding.ETDFrom.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please set date From first", Toast.LENGTH_SHORT).show()
            } else {
                val datees2 = DatePickerDialog(
                    this@DetailActivity,
                    date2,
                    myCalendarTo[Calendar.YEAR],
                    myCalendarTo[Calendar.MONTH],
                    myCalendarTo[Calendar.DAY_OF_MONTH]
                )
                // Disable dates before the selected date in ETDFrom
                val dateFormatFrom = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val minDate = dateFormatFrom.parse(binding.ETDFrom.text.toString())?.time
                Log.d("minDate", "$minDate")
                if (minDate != null) {
                    datees2.datePicker.minDate = minDate
                }
                datees2.show()
            }
        }

        //save car for future bookings
        binding.ibSave.setOnClickListener {
            val today: LocalDate = LocalDate.now()
            val userId = pref.getUSERID()
            val saveInfo = SaveCar(car?.car_id, userId, "$today")

            apiClient.getApiService().saveCar(saveInfo).enqueue(object : Callback<SaveCarResponse> {
                override fun onResponse(call: Call<SaveCarResponse>, response: Response<SaveCarResponse>) {
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

        //make car booking
        binding.btBook.setOnClickListener {
            if ("${binding.ETDFrom.text}".trim() == "") {
                binding.ETDFrom.error = "Kindly choose from date"
            } else if ("${binding.ETDTo.text}".trim() == "") {
                binding.ETDTo.error = "Kindly choose to date"
            } else if (binding.radioDriveGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this@DetailActivity, "Kindly choose drive mode", Toast.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty("${binding.etDestination.text}".trim())) {
                binding.etDestination.error = "Kindly enter a destination!"
            } else {
                //selected radio button
                if (binding.radioSelfDrive.isChecked) {
                    selectedDrive = "${binding.radioSelfDrive.text}"
                } else if (binding.radioChauffeured.isChecked) {
                    selectedDrive = "${binding.radioChauffeured.text}"
                }

                if ("$firstName" != "") {
                    val intent = Intent(this, PaymentActivity::class.java)
                    val bookDateFrom = "${binding.ETDFrom.text}".trim()
                    val bookDateTo = "${binding.ETDTo.text}".trim()
                    val destination = "${binding.etDestination.text}".trim()
                    val drive = selectedDrive
                    val totalDays = "${binding.tvTotalDays.text}".trim()
                    val totalAmount = total
                    intent.putExtra("car_name", "${binding.tvCarName.text}")
                    intent.putExtra("drive", "$drive")
                    intent.putExtra("car_id", "${car?.car_id}")
                    intent.putExtra("book_date_from", bookDateFrom)
                    intent.putExtra("book_date_to", bookDateTo)
                    intent.putExtra("destination", destination)
                    intent.putExtra("total_days", totalDays)
                    intent.putExtra("total_amount", totalAmount)
                    intent.putExtra("amntPerDay", "${binding.tvPrice.text}")

                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.nav.setOnClickListener {
            if ("$firstName" != "") {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        if ("$firstName" != "") {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END)
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
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START)
            binding.outNavView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
                Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)

                when (item.itemId) {
                    R.id.login -> {
                        val intentLogin = Intent(this, LoginActivity::class.java)
                        startActivity(intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.createAccount -> {
                        val intentProfile = Intent(this, RegisterActivity::class.java)
                        startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.about -> {
                        val intentAbout = Intent(this, AboutActivity::class.java)
                        startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.help -> {
                        val intentHelp = Intent(this, LoginActivity::class.java)
                        startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        return@OnNavigationItemSelectedListener true
                    }
                }
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
                false
            })
        }

        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun restartApp() {
        finish()
        startActivity(intent)
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
        val price = binding.tvPrice.text.toString()
        val noOfDays = binding.tvTotalDays.text.toString()
        val finalPrice = price.toInt()
        val finalNoOfDays = noOfDays.toInt()

        total = StringBuilder().apply { append(finalPrice * finalNoOfDays) }.toString()
        binding.tvTotalAmount.text = getString(R.string.total_amount, total)
    }
}