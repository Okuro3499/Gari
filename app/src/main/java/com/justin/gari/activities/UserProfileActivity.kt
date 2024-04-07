package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityProfileCompleteBinding
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.utils.SharedPrefManager
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {
    lateinit var pref: SharedPrefManager
    lateinit var apiClient: ApiClient
    lateinit var binding: ActivityProfileCompleteBinding
    private var userId :String? = null
    private var roleId :String? = null
    var profileHeader:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref.loadNightModeState()}")
        if (pref.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityProfileCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        userId = pref.getUSERID()
        roleId = pref.getROLEID()

        profileHeader = pref.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
        val lastNameTextView = header.findViewById<TextView>(R.id.lastName)
        val emailTextView = header.findViewById<TextView>(R.id.email)
        val profileImage = header.findViewById<CircleImageView>(R.id.profile_image)
        val inSwitch = header.findViewById<SwitchCompat>(R.id.themeSwitch)
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
            inSwitch.text = getString(R.string.light_mode)
        } else{
            inSwitch.text = getString(R.string.dark_mode)
        }

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(binding.profilePic)

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

        binding.ltEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.ltUserSettings.setOnClickListener {
            val intent = Intent(this, UserSettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        binding.ltEmergency.setOnClickListener {
            val intent = Intent(this, EmergencyContactsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

//        binding.btSubmit.setOnClickListener {
//            val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//            val user_id = sharedPreferences.getString("user_id", "default")
//
//            val progressDialog5 = ProgressDialog(this@UserProfileActivity)
//            progressDialog5.setCancelable(false) // set cancelable to false
//            progressDialog5.setMessage("Uploading...") // set message
//            progressDialog5.show()
//            val contactInfo = Contacts(
//                binding.etFullName.text.toString().trim(),
//                binding.etRelationShip.text.toString().trim(),
//                binding.etEmergencyMobile.text.toString().trim(),
//                binding.etFullName2.text.toString().trim(),
//                binding.etRelationShip2.text.toString().trim(),
//                binding.etEmergencyMobile2.text.toString().trim()
//            )
//
//            Log.e("Gideon", "onSuccess:$contactInfo")
//
//            apiClient.getApiService(this).contactUpdate(user_id, contactInfo).enqueue(object : Callback<UserDetailsResponse> {
//                    override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
//                        if (response.isSuccessful) {
//                            progressDialog5.dismiss()
//                            Toast.makeText(this@UserProfileActivity, "Uploaded Successfully", Toast.LENGTH_LONG).show()
//                            Log.e("Gideon", "onSuccess: ${response.body()}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
//                        Toast.makeText(this@UserProfileActivity, "${t.message}", Toast.LENGTH_LONG).show()
//                        Log.e("Gideon", "onFailure: ${t.message}")
//                    }
//                })
//        }
//
//        binding.ivDl.setOnClickListener { ImagePicker.with(this).start(0) }
//
//        binding.ivId.setOnClickListener { ImagePicker.with(this).start(1) }
//
//        binding.ivPhoto.setOnClickListener { ImagePicker.with(this).start(2) }


        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

        apiClient.getApiService().getUserDetails(userId, roleId).enqueue(object : Callback<UserDetailsResponse> {
            override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    Log.e("Gideon", "onSuccess: ${response.body()}")
                    val firstName = response.body()?.single_user?.first_name ?: ""
                    val lastName = response.body()?.single_user?.last_name ?: ""
                    val userName = getString(R.string.user_name, firstName, lastName)
                    binding.tvName.text = userName
                    binding.tvEmail.text = "${response.body()!!.single_user.email}"
                    binding.tvMobile.text = "${response.body()!!.single_user.mobile}"
                    binding.tvCounty.text = "${response.body()!!.single_user.county}"
                    binding.tvDistrict.text = "${response.body()!!.single_user.district}"
                    binding.tvEstate.text = "${response.body()!!.single_user.estate}"
                    binding.tvLandMark.text = "${response.body()!!.single_user.landmark}"
                    Picasso.get()
                        .load(profileHeader)
                        .fit().centerCrop()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(binding.profilePic)

//                        binding.etFullName.setText(response.body()!!.single_user.contact1_name.toString())
//                        binding.etRelationShip.setText(response.body()!!.single_user.contact1_relationship.toString())
//                        binding.etEmergencyMobile.setText(response.body()!!.single_user.contact1_mobile.toString())
//                        binding.etFullName2.setText(response.body()!!.single_user.contact2_name.toString())
//                        binding.etRelationShip2.setText( response.body()!!.single_user.contact2_relationship.toString())
//                        binding.etEmergencyMobile2.setText(response.body()!!.single_user.contact1_mobile.toString())

//                        //driver license
//                        Picasso.get()
//                            .load(response.body()!!.single_user.driver_licence_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivDl)
//
//                        //national id
//                        Picasso.get()
//                            .load(response.body()!!.single_user.national_id_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivId)
//
//                        //userphoto
//                        Picasso.get()
//                            .load(response.body()!!.single_user.user_photo_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivPhoto)
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
//        val imageIdCardPicker = findViewById<ImageView>(R.id.ivId)
//        val userPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)
//
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 0) {
//            if (resultCode == Activity.RESULT_OK) {
//                //Image Uri will not be null for RESULT_OK
//                val uri: Uri = data?.data!!
//                // Use Uri object instead of File to avoid storage permissions
//                imageDriverLicensePicker.setImageURI(data.data!!)
//                val uriPathHelper = URIPathHelper()
//                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line
//
//                Log.i("FilePath", filePath)
//                val file = File(filePath)
//                val driverLicense: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//                val dlButton = findViewById<Button>(R.id.btDlUpload)
//                dlButton.setOnClickListener { uploadDl(file, driverLicense) }
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }
//        if (requestCode == 1) {
//            if (resultCode == RESULT_OK) {
//                //Image Uri will not be null for RESULT_OK
//                val uri: Uri = data?.data!!
//                // Use Uri object instead of File to avoid storage permissions
//                imageIdCardPicker.setImageURI(data.data!!)
//                val uriPathHelper = URIPathHelper()
//                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line
//
//                Log.i("FilePath", filePath)
//                val file = File(filePath)
//                val identityCard: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//                val btIdUpload = findViewById<Button>(R.id.btIdUpload)
//                btIdUpload.setOnClickListener { uploadId(file, identityCard) }
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }
//        if (requestCode == 2) {
//            if (resultCode == RESULT_OK) {
//                //Image Uri will not be null for RESULT_OK
//                val uri: Uri = data?.data!!
//                // Use Uri object instead of File to avoid storage permissions
//                userPhotoPicker.setImageURI(data.data!!)
//                val uriPathHelper = URIPathHelper()
//                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line
//
//                Log.i("FilePath", filePath)
//                val file = File(filePath)
//                val userPhoto: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//                val btUserPhotoUpload = findViewById<Button>(R.id.btUserPhotoUpload)
//                btUserPhotoUpload.setOnClickListener { uploadPhoto(file, userPhoto) }
//            }
//            else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}