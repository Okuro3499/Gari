package com.justin.gari.activities

import android.app.Activity
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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.URIPathHelper
import com.justin.gari.api.ApiClient
import com.justin.gari.models.uploadImagesModel.Contacts
import com.justin.gari.models.uploadImagesModel.DlCloudinaryResponse
import com.justin.gari.models.uploadImagesModel.ImageInfoResponse
import com.justin.gari.models.userModels.UserDetailsResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileCompleteActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_complete)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val firstName = findViewById<TextView>(R.id.tvFirstName)
        val lastName = findViewById<TextView>(R.id.tvLastName)
        val email = findViewById<TextView>(R.id.tvEmail)
        val mobile = findViewById<TextView>(R.id.tvMobile)
        val county = findViewById<TextView>(R.id.tvCounty)
        val district = findViewById<TextView>(R.id.tvDistrict)
        val estate = findViewById<TextView>(R.id.tvEstate)
        val landMark = findViewById<TextView>(R.id.tvLandMark)
        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
        val imageIdCardPicker = findViewById<ImageView>(R.id.ivId)
        val userPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)

        val clientId = sharedPreferences.getString("client_id", "default")

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val firstNameHeader = sharedPreferences.getString("first_name", "default")
        val lastNameHeader = sharedPreferences.getString("last_name", "default")
        val emailHeader = sharedPreferences.getString("email", "default")
        val header: View = navView.getHeaderView(0)
        val firstNameTv = header.findViewById<View>(R.id.firstName) as TextView
        firstNameTv.text = firstNameHeader.toString()
        val lastNameTv = header.findViewById<View>(R.id.lastName) as TextView
        lastNameTv.text = lastNameHeader.toString()
        val emailTv = header.findViewById<View>(R.id.email) as TextView
        emailTv.text = emailHeader.toString()

        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            Log.i(ContentValues.TAG, "onNavigationItemSelected: " + item.itemId)
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@ProfileCompleteActivity, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile =
                        Intent(this@ProfileCompleteActivity, ProfileCompleteActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles =
                        Intent(this@ProfileCompleteActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin =
                        Intent(this@ProfileCompleteActivity, LoginActivity::class.java)
                    startActivity(intentLogin)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout =
                        Intent(this@ProfileCompleteActivity, AboutActivity::class.java)
                    startActivity(intentAbout)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@ProfileCompleteActivity, AboutActivity::class.java)
                    startActivity(intentHelp)
                    return@OnNavigationItemSelectedListener true
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

        apiClient = ApiClient
        apiClient.getApiService(this).getUserDetails(clientId)
            .enqueue(object : Callback<UserDetailsResponse> {
                override fun onResponse(
                    call: Call<UserDetailsResponse>,
                    response: Response<UserDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        firstName.text = response.body()!!.single_client.first_name.toString()
                        lastName.text = response.body()!!.single_client.last_name.toString()
                        email.text = response.body()!!.single_client.email.toString()
                        mobile.text = response.body()!!.single_client.mobile.toString()
                        county.text = response.body()!!.single_client.county.toString()
                        district.text = response.body()!!.single_client.district.toString()
                        estate.text = response.body()!!.single_client.estate.toString()
                        landMark.text = response.body()!!.single_client.landmark.toString()
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })


        val button = findViewById<Button>(R.id.btSubmit)
        button.setOnClickListener {
            val id = clientId.toString().trim()
            val name1 = findViewById<EditText>(R.id.etFullName).text.toString().trim()
            val relation1 = findViewById<EditText>(R.id.etRelationShip).text.toString().trim()
            val contact1 = findViewById<EditText>(R.id.etEmergencyMobile).text.toString().trim()
            val name2 = findViewById<EditText>(R.id.etFullName2).text.toString().trim()
            val relation2 = findViewById<EditText>(R.id.etRelationShip2).text.toString().trim()
            val contact2 = findViewById<EditText>(R.id.etEmergencyMobile2).text.toString().trim()

            val idClient = Contacts(id, name1, relation1, contact1, name2, relation2, contact2)
            Toast.makeText(
                this@ProfileCompleteActivity,
                "clicked",
                Toast.LENGTH_LONG
            ).show()
            apiClient.getApiService(this).contacts(idClient)
                .enqueue(object : Callback<ImageInfoResponse> {
                    override fun onResponse(
                        call: Call<ImageInfoResponse>,
                        response: Response<ImageInfoResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.e(
                                "Gideon",
                                "onSuccess: ${response.body()}"
                            )
                            Toast.makeText(
                                this@ProfileCompleteActivity,
                                "Upload Successful",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<ImageInfoResponse>, t: Throwable) {
                        Log.e("Gideon", "onFailure: ${t.message}")
                    }
                })
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
        }

        imageDriverLicensePicker.setOnClickListener {
            ImagePicker.with(this)
                .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)

        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
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
                dlButton.setOnClickListener {
                    uploadDl(file, driverLicense)
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadDl(file: File, driverLicense: RequestBody) {
        val image = MultipartBody.Part.createFormData("image", file.name, driverLicense)
        apiClient.getApiService(this).dlCloudinary(image)
            .enqueue(object : Callback<DlCloudinaryResponse> {
                override fun onResponse(
                    call: Call<DlCloudinaryResponse>,
                    response: Response<DlCloudinaryResponse>
                ) {
                    Log.e("Gideon", "cloudinary: $response")
                    if (response.isSuccessful) {
                        val dlUrl = response.body()!!.driverLicenceCloudinary
                        Log.e("Gideon", "dlUrl: $dlUrl")
//                    apiClient.getApiService(this@ProfileCompleteActivity).dlDatabase(dlUrl).enqueue(object : Callback<ImageInfoResponse> {
//                        override fun onResponse(call: Call<ImageInfoResponse>, response: Response<ImageInfoResponse>) {
//                            if (response.isSuccessful) {
//                                Toast.makeText(this@ProfileCompleteActivity, "Upload Successful", Toast.LENGTH_LONG).show()
//                                Log.e("Gideon", "onSuccess: ${response.body()}")
//                            }
//                        }
//
//                        override fun onFailure(call: Call<ImageInfoResponse>, t: Throwable) {
//                            Toast.makeText(this@ProfileCompleteActivity,"${t.message}", Toast.LENGTH_LONG).show()
//                            Log.e("Gideon", "onFailure: ${t.message}")
//                        }
//                    })
                    }
                }

                override fun onFailure(call: Call<DlCloudinaryResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

}