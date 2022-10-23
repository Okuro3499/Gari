package com.justin.gari.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.URIPathHelper
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityUserSettingsBinding
import com.justin.gari.models.uploadImagesModel.*
import com.justin.gari.models.userModels.UserDetailsResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserSettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserSettingsBinding
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityUserSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val clientId = sharedPreferences.getString("client_id", "default")

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val profileHeader = sharedPreferences.getString("userProfile", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "default")
        val lastNameHeader = sharedPreferences.getString("last_name", "default")
        val emailHeader = sharedPreferences.getString("email", "default")
        val header: View = binding.navView.getHeaderView(0)
        val profileImage = header.findViewById(R.id.profile_image) as CircleImageView
        val firstNameTv = header.findViewById<View>(R.id.firstName) as TextView
        firstNameTv.text = firstNameHeader.toString()
        val lastNameTv = header.findViewById<View>(R.id.lastName) as TextView
        lastNameTv.text = lastNameHeader.toString()
        val emailTv = header.findViewById<View>(R.id.email) as TextView
        emailTv.text = emailHeader.toString()

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profileImage)

        val switchTheme = header.findViewById(R.id.themeSwitch) as Switch
        if (settingsManager.loadNightModeState()) {
            switchTheme.isChecked = true
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
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
            //TODO: set visibility
            when (item.itemId) {
                R.id.home -> {
                    startActivity(
                        Intent(this@UserSettingsActivity, MainActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@UserSettingsActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles =
                        Intent(this@UserSettingsActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@UserSettingsActivity, LoginActivity::class.java)
                    startActivity(intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@UserSettingsActivity, AboutActivity::class.java)
                    startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@UserSettingsActivity, LoginActivity::class.java)
                    startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

        binding.back.setOnClickListener {
            val intent = Intent(this@UserSettingsActivity, UserProfileActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        apiClient = ApiClient
        apiClient.getApiService(this).getUserDetails(clientId).enqueue(object : Callback<UserDetailsResponse> {
                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        //driver license
                        Picasso.get()
                            .load(response.body()!!.single_client.driver_licence_url)
                            .fit().centerCrop()
                            .placeholder(R.drawable.click)
                            .error(R.drawable.click)
                            .into(binding.ivDl)

                        //national id
                        Picasso.get()
                            .load(response.body()!!.single_client.national_id_url)
                            .fit().centerCrop()
                            .placeholder(R.drawable.click)
                            .error(R.drawable.click)
                            .into(binding.ivId)

                        //userphoto
                        Picasso.get()
                            .load(response.body()!!.single_client.user_photo_url)
                            .fit().centerCrop()
                            .placeholder(R.drawable.click)
                            .error(R.drawable.click)
                            .into(binding.ivPhoto)
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        binding.ivDl.setOnClickListener { ImagePicker.with(this).start(0) }

        binding.ivId.setOnClickListener { ImagePicker.with(this).start(1) }

        binding.ivPhoto.setOnClickListener { ImagePicker.with(this).start(2) }

    }

    private fun restartApp() {
        recreate()
//        val i = Intent(applicationContext, UserProfileActivity::class.java)
//        startActivity(i)
//        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
        val imageIdCardPicker = findViewById<ImageView>(R.id.ivId)
        val userPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                imageDriverLicensePicker.setImageURI(data.data!!)
                val uriPathHelper = URIPathHelper()
                val filePath = uriPathHelper.getPath(this, uri)!! //try and fix this line

                Log.i("FilePath", filePath)
                val file = File(filePath)
                val driverLicense: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

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
                val identityCard: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

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
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id", "default")

        val progressDialog0 = ProgressDialog(this@UserSettingsActivity)
        progressDialog0.setCancelable(false) // set cancelable to false
        progressDialog0.setMessage("Uploading...") // set message
        progressDialog0.show()

        val image = MultipartBody.Part.createFormData("image", file.name, driverLicense)
        apiClient.getApiService(this).dlCloudinary(image).enqueue(object : Callback<DlCloudinaryResponse> {
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

                        apiClient.getApiService(this@UserSettingsActivity).dlCloudinaryResponseToDb(client_id, dlUrl).enqueue(object : Callback<UserDetailsResponse> {
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
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id", "default")

        val progressDialog2 = ProgressDialog(this@UserSettingsActivity)
        progressDialog2.setCancelable(false) // set cancelable to false
        progressDialog2.setMessage("Uploading...") // set message
        progressDialog2.show()

        val image = MultipartBody.Part.createFormData("image", file.name, identityCard)
        apiClient.getApiService(this).idCloudinary(image).enqueue(object : Callback<IdCloudinaryResponse> {
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

                        apiClient.getApiService(this@UserSettingsActivity).nationalIdCloudinaryResponseToDb(client_id, nationalIdUrl).enqueue(object : Callback<UserDetailsResponse> {
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
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val client_id = sharedPreferences.getString("client_id", "default")
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val progressDialog4 = ProgressDialog(this@UserSettingsActivity)
        progressDialog4.setCancelable(false) // set cancelable to false
        progressDialog4.setMessage("Uploading...") // set message
        progressDialog4.show()

        val image = MultipartBody.Part.createFormData("image", file.name, userPhoto)
        apiClient.getApiService(this).userPhotoCloudinary(image).enqueue(object : Callback<UserPhotoCloudinaryResponse> {
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
                        editor.putString("userProfile", response.body()!!.userPhotoCloudinary)
                        editor.apply()
                        apiClient.getApiService(this@UserSettingsActivity).userPhotoCloudinaryResponseToDb(client_id, userPhotoUrl).enqueue(object : Callback<UserDetailsResponse> {
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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)) {
//            true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}