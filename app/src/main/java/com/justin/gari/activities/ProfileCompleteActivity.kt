package com.justin.gari.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.models.uploadImagesModel.ImageInfoResponse
import com.justin.gari.models.uploadImagesModel.UploadDlResponse
import com.justin.gari.models.userModels.UserDetailsResponse
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import kotlinx.android.synthetic.main.activity_profile_complete.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import retrofit2.Retrofit
import java.io.File

class ProfileCompleteActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient
    var config: HashMap<String, String> = HashMap()
    var imgpath: String? = null

//    var imageDriverLicensePicker:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_complete)
        config["cloud_name"] = "okuro"
        config["api_key"] = "639124242572386"
        config["api_secret"] = "jbUm8UuCamH5sRzonkKkV_3a1n8"
        config["upload_preset"] = "default_preset"
        MediaManager.init(this, config)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val firstName = findViewById<TextView>(R.id.tvFirstName)
        val lastName = findViewById<TextView>(R.id.tvLastName)
        val email = findViewById<TextView>(R.id.tvEmail)
        val mobile = findViewById<TextView>(R.id.tvMobile)
        val county = findViewById<TextView>(R.id.tvCounty)
        val district = findViewById<TextView>(R.id.tvDistrict)
        val estate = findViewById<TextView>(R.id.tvEstate)
        val landMark = findViewById<TextView>(R.id.tvLandMark)

        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
//        val imageNationalIdPicker = findViewById<ImageView>(R.id.ivId)
//        val imageUserPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)
//
//        imageDriverLicensePicker.setOnClickListener{
//            ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop().maxResultSize(210,150).start()
//        }
//
//        imageNationalIdPicker.setOnClickListener{
//            ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop().maxResultSize(210,150).start()
//        }
//
//        imageUserPhotoPicker.setOnClickListener{
//            ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*")).crop().maxResultSize(210,150).start()
//        }

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
                    val intentProfile = Intent(this@ProfileCompleteActivity, ProfileCompleteActivity::class.java)
                    startActivity(intentProfile)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles = Intent(this@ProfileCompleteActivity, MyVehiclesActivity::class.java)
                    startActivity(intentMyVehicles)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    val intentLogin = Intent(this@ProfileCompleteActivity, LoginActivity::class.java)
                    startActivity(intentLogin)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@ProfileCompleteActivity, AboutActivity::class.java)
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
        apiClient.getApiService(this).getUserDetails(clientId).enqueue(object : Callback<UserDetailsResponse> {
                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
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

        apiClient.getApiService(this).addClientId(clientId).enqueue(object : Callback<ImageInfoResponse> {
            override fun onResponse(call: Call<ImageInfoResponse>, response: Response<ImageInfoResponse>) {
                if (response.isSuccessful) {
                    Log.e("Gideon", "onSuccess: ${response.body()}")
                }
            }
            override fun onFailure(call: Call<ImageInfoResponse>, t: Throwable) {
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })

        val dlButton = findViewById<Button>(R.id.btDlUpload)
        dlButton.setOnClickListener{
            uploadToCloudinary(imgpath.toString())
//            apiClient.getApiService(this).uploadDl().enqueue(object : Callback<UploadDlResponse> {
//                override fun onResponse(call: Call<UploadDlResponse>, response: Response<UploadDlResponse>) {
//                    if (response.isSuccessful) {
//                        Log.e("Gideon", "onSuccess: ${response.body()}")
//
//                    }
//                }
//                override fun onFailure(call: Call<UploadDlResponse>, t: Throwable) {
//                    Log.e("Gideon", "onFailure: ${t.message}")
//                }
//            })
        }

        val button = findViewById<Button>(R.id.btSubmit)
        button.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        imageDriverLicensePicker.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


    }

    fun uploadToCloudinary(filepath: String) {
        MediaManager.get().upload(filepath).unsigned("sample_app_preset").callback(object : UploadCallback {
            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                Toast.makeText(applicationContext, "Task successful", Toast.LENGTH_SHORT).show()
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {

            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                error
                Toast.makeText(applicationContext, "Task Not successful"+ error, Toast.LENGTH_SHORT).show()
            }

            override fun onStart(requestId: String?) {

                Toast.makeText(applicationContext, "Start", Toast.LENGTH_SHORT).show()
            }
        }).dispatch()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            imageDriverLicensePicker.setImageURI(fileUri)

            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath: String = ImagePicker.getFilePath(data)!!
            imgpath = filePath
            uploadToCloudinary(filePath)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Log.e("cloudinary", "failed: ${ImagePicker.getError(data)}")
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val imageDriverLicensePicker = findViewById<ImageView>(R.id.ivDl)
//        val imageNationalIdPicker = findViewById<ImageView>(R.id.ivId)
//        val imageUserPhotoPicker = findViewById<ImageView>(R.id.ivPhoto)
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
//            imageDriverLicensePicker?.setImageURI(data?.data)
//
//        }
//
//        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
//            imageNationalIdPicker?.setImageURI(data?.data)
//        }
//
//        if(resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
//            imageUserPhotoPicker?.setImageURI(data?.data)
//        }
//
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}



