package com.justin.gari.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityUserSettingsBinding
import com.justin.gari.models.uploadImagesModel.DlCloudinaryResponse
import com.justin.gari.models.uploadImagesModel.DlUrl
import com.justin.gari.models.uploadImagesModel.IdCloudinaryResponse
import com.justin.gari.models.uploadImagesModel.NationalIdUrl
import com.justin.gari.models.uploadImagesModel.UserPhotoCloudinaryResponse
import com.justin.gari.models.uploadImagesModel.UserPhotoUrl
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.utils.SharedPrefManager
import com.justin.gari.utils.URIPathHelper
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class UserSettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserSettingsBinding
    lateinit var pref: SharedPrefManager
    private lateinit var apiClient: ApiClient
    private var userId :String? = null
    private var roleId :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref.loadNightModeState()}")
        if (pref.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        userId = pref.getUSERID()
        roleId = pref.getROLEID()

        val profileHeader = pref.getUSERPROFILEPHOTO()
        val header = binding.navView.getHeaderView(0)
        val firstNameTextView = header.findViewById<TextView>(R.id.firstName)
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

        binding.ivDl.setOnClickListener { ImagePicker.with(this).start(0) }

        binding.ivId.setOnClickListener { ImagePicker.with(this).start(1) }

        binding.ivPhoto.setOnClickListener { ImagePicker.with(this).start(2) }

        binding.back.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
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
                    //driver license
                    Picasso.get()
                        .load(response.body()!!.single_user.driver_licence_url)
                        .fit().centerCrop()
                        .placeholder(R.drawable.click)
                        .error(R.drawable.click)
                        .into(binding.ivDl)

                    //national id
                    Picasso.get()
                        .load(response.body()!!.single_user.national_id_url)
                        .fit().centerCrop()
                        .placeholder(R.drawable.click)
                        .error(R.drawable.click)
                        .into(binding.ivId)

                    //userphoto
                    Picasso.get()
                        .load(response.body()!!.single_user.user_photo_url)
                        .fit().centerCrop()
                        .placeholder(R.drawable.click)
                        .error(R.drawable.click)
                        .into(binding.ivPhoto)
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
        val imageIdCardPicker = findViewById<ImageView>(R.id.ivId)
        val userPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("data", "$data")

                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                binding.ivDl.setImageURI(data.data!!)
                val uriPathHelper = URIPathHelper()
                Log.i("uri", "$uri")
                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line

                Log.i("FilePath", filePath)
                val file = File(filePath)

                try {
                    val fis = FileInputStream(file)
                    val baos = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (fis.read(buffer).also { len = it } > -1) {
                        baos.write(buffer, 0, len)
                    }
                    baos.flush()
                    val imageBase64: String = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
                    Log.d("IMAGE", imageBase64)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val driverLicense: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                val dlButton = findViewById<Button>(R.id.btDlUpload)
                dlButton.setOnClickListener { uploadDl(file, driverLicense) }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                imageIdCardPicker.setImageURI(data.data!!)

                val uriPathHelper = URIPathHelper()
                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line

                Log.i("FilePath", filePath)
                val file = File(filePath)
                val identityCard: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                val btIdUpload = findViewById<Button>(R.id.btIdUpload)
                btIdUpload.setOnClickListener { uploadId(file, identityCard) }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                userPhotoPicker.setImageURI(data.data!!)
                val uriPathHelper = URIPathHelper()
                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line

                Log.i("FilePath", filePath)
                val file = File(filePath)
                val userPhoto: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                val btUserPhotoUpload = findViewById<Button>(R.id.btUserPhotoUpload)
                btUserPhotoUpload.setOnClickListener { uploadPhoto(file, userPhoto) }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadDl(file: File, driverLicense: RequestBody) {
        val progressDialog0 = ProgressDialog(this@UserSettingsActivity)
        progressDialog0.setCancelable(false) // set cancelable to false
        progressDialog0.setMessage("Uploading...") // set message
        progressDialog0.show()

        val image = MultipartBody.Part.createFormData("image", file.name, driverLicense)
        apiClient.getApiService().dlCloudinary(image).enqueue(object : Callback<DlCloudinaryResponse> {
                override fun onResponse(call: Call<DlCloudinaryResponse>, response: Response<DlCloudinaryResponse>) {
                    Log.e("Gideon", "cloudinary: $response")
                    if (response.isSuccessful) {
                        progressDialog0.dismiss()
                        Toast.makeText(this@UserSettingsActivity, "Driver license uploaded successful", Toast.LENGTH_SHORT).show()
                        Log.e("Gideon", "cloudinaryresponse: ${response.body()!!.driverLicenceCloudinary}")

                        val progressDialog1 = ProgressDialog(this@UserSettingsActivity)
                        progressDialog1.setCancelable(false) // set cancelable to false
                        progressDialog1.setMessage("Securing image...") // set message
                        progressDialog1.show()

                        val dlUrl = DlUrl(response.body()!!.driverLicenceCloudinary)

                        apiClient.getApiService().dlCloudinaryResponseToDb(pref.getUSERID(), dlUrl).enqueue(object : Callback<UserDetailsResponse> {
                                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                                    if (response.isSuccessful) {
                                        progressDialog1.dismiss()
                                        Toast.makeText(this@UserSettingsActivity, "Image Secured", Toast.LENGTH_SHORT).show()
                                        Log.e("Gideon", "onSuccess: ${response.body()}")
                                    }
                                }

                                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                                    Log.e("Gideon", "onFailure: ${t.message}")
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<DlCloudinaryResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
    }

    private fun uploadId(file: File, identityCard: RequestBody) {
        val progressDialog2 = ProgressDialog(this@UserSettingsActivity)
        progressDialog2.setCancelable(false) // set cancelable to false
        progressDialog2.setMessage("Uploading...") // set message
        progressDialog2.show()

        val image = MultipartBody.Part.createFormData("image", file.name, identityCard)
        apiClient.getApiService().idCloudinary(image).enqueue(object : Callback<IdCloudinaryResponse> {
            override fun onResponse(call: Call<IdCloudinaryResponse>, response: Response<IdCloudinaryResponse>) {
                Log.e("Gideon", "cloudinary: $response")
                if (response.isSuccessful) {
                    progressDialog2.dismiss()
                    Toast.makeText(this@UserSettingsActivity, "national id uploaded successfully", Toast.LENGTH_SHORT).show()
                    val nationalIdUrl = NationalIdUrl(response.body()!!.nationalIdCloudinary)
                        val progressDialog3 = ProgressDialog(this@UserSettingsActivity)
                        progressDialog3.setCancelable(false) // set cancelable to false
                        progressDialog3.setMessage("Securing image...") // set message
                        progressDialog3.show()

                        apiClient.getApiService()
                            .nationalIdCloudinaryResponseToDb(pref.getUSERID(), nationalIdUrl).enqueue(object : Callback<UserDetailsResponse> {
                                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                                    if (response.isSuccessful) {
                                        progressDialog3.dismiss()
                                        Toast.makeText(this@UserSettingsActivity, "Image Secured", Toast.LENGTH_SHORT).show()
                                        Log.e("Gideon", "onSuccess: ${response.body()}")
                                    }
                                }

                                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                                    Log.e("Gideon", "onFailure: ${t.message}")
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<IdCloudinaryResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
    }

    private fun uploadPhoto(file: File, userPhoto: RequestBody) {
        val progressDialog4 = ProgressDialog(this@UserSettingsActivity)
        progressDialog4.setCancelable(false) // set cancelable to false
        progressDialog4.setMessage("Uploading...") // set message
        progressDialog4.show()

        val image = MultipartBody.Part.createFormData("image", file.name, userPhoto)
        apiClient.getApiService().userPhotoCloudinary(image).enqueue(object : Callback<UserPhotoCloudinaryResponse> {
                override fun onResponse(call: Call<UserPhotoCloudinaryResponse>, response: Response<UserPhotoCloudinaryResponse>) {
                    Log.e("Gideon", "cloudinary: $response")
                    if (response.isSuccessful) {
                        progressDialog4.dismiss()
                        Toast.makeText(this@UserSettingsActivity, "User Photo uploaded successfully", Toast.LENGTH_SHORT).show()
                        val progressDialog5 = ProgressDialog(this@UserSettingsActivity)
                        progressDialog5.setCancelable(false) // set cancelable to false
                        progressDialog5.setMessage("Securing image...") // set message
                        progressDialog5.show()

                        val userPhotoUrl = UserPhotoUrl(response.body()!!.userPhotoCloudinary)
                        pref.setUSERPROFILEPHOTO(response.body()!!.userPhotoCloudinary)
                        apiClient.getApiService()
                            .userPhotoCloudinaryResponseToDb(pref.getUSERID(), userPhotoUrl).enqueue(object : Callback<UserDetailsResponse> {
                                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                                    if (response.isSuccessful) {
                                        progressDialog5.dismiss()
                                        Toast.makeText(this@UserSettingsActivity, "Image Secured", Toast.LENGTH_SHORT).show()
                                        Log.e("Gideon", "onSuccess: ${response.body()}")
                                    }
                                }

                                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                                    Log.e("Gideon", "onFailure: ${t.message}")
                                }
                            })
                    }
                }

                override fun onFailure(call: Call<UserPhotoCloudinaryResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}