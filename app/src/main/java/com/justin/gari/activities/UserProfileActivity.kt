package com.justin.gari.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.justin.gari.R
import com.justin.gari.utils.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityProfileCompleteBinding
import com.justin.gari.models.userModels.UserDetailsResponse
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_profile_complete.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    val sharedPrefFile = "sharedPrefData"
    lateinit var apiClient: ApiClient
    var theme: Switch? = null
    lateinit var settingsManager: SettingsManager
    lateinit var binding: ActivityProfileCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        }
        else
            setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val clientId = sharedPreferences.getString("client_id", "default")

//        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
//        binding.drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
        apiClient = ApiClient

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        val profileHeader = sharedPreferences.getString("userProfile", "default")
        val firstNameHeader = sharedPreferences.getString("first_name", "")
        val lastNameHeader = sharedPreferences.getString("last_name", "")
        val emailHeader = sharedPreferences.getString("email", "")
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

        Picasso.get()
            .load(profileHeader)
            .fit().centerCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(profilePic)

        val switchTheme = header.findViewById(R.id.themeSwitch) as Switch
        if (settingsManager.loadNightModeState()) {
            switchTheme.isChecked = true
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingsManager.setNightModeState(true)
                restartApp()
            }
            else {
                settingsManager.setNightModeState(false)
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
                    startActivity(Intent(this@UserProfileActivity, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    val intentProfile = Intent(this@UserProfileActivity, UserProfileActivity::class.java)
                    startActivity(intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myVehicles -> {
                    val intentMyVehicles = Intent(this@UserProfileActivity, VehiclesActivity::class.java)
                    startActivity(intentMyVehicles.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.logout -> {
                    editor.clear()
                    editor.apply()
                    val intentLogout = Intent(this@UserProfileActivity, MainActivity::class.java)
                    startActivity(intentLogout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    finish()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    val intentAbout = Intent(this@UserProfileActivity, AboutActivity::class.java)
                    startActivity(intentAbout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.help -> {
                    val intentHelp = Intent(this@UserProfileActivity, LoginActivity::class.java)
                    startActivity(intentHelp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    return@OnNavigationItemSelectedListener true
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            Log.i(ContentValues.TAG, "onNavigationItemSelected: nothing clicked")
            false
        })

        binding.back.setOnClickListener{
            val intent = Intent(this@UserProfileActivity, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        apiClient.getApiService(this).getUserDetails(clientId).enqueue(object : Callback<UserDetailsResponse> {
                override fun onResponse(call: Call<UserDetailsResponse>, response: Response<UserDetailsResponse>) {
                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        binding.tvName.text = response.body()!!.single_client.first_name.toString() + " " + response.body()!!.single_client.last_name.toString()
//                        binding.tvLastName.text = response.body()!!.single_client.last_name.toString()
                        binding.tvEmail.text = response.body()!!.single_client.email.toString()
                        binding.tvMobile.text = response.body()!!.single_client.mobile.toString()
                        binding.tvCounty.text = response.body()!!.single_client.county.toString()
                        binding.tvDistrict.text = response.body()!!.single_client.district.toString()
                        binding.tvEstate.text = response.body()!!.single_client.estate.toString()
                        binding.tvLandMark.text = response.body()!!.single_client.landmark.toString()
                        Picasso.get()
                            .load(profileHeader)
                            .fit().centerCrop()
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(binding.profilePic)

//                        binding.etFullName.setText(response.body()!!.single_client.contact1_name.toString())
//                        binding.etRelationShip.setText(response.body()!!.single_client.contact1_relationship.toString())
//                        binding.etEmergencyMobile.setText(response.body()!!.single_client.contact1_mobile.toString())
//                        binding.etFullName2.setText(response.body()!!.single_client.contact2_name.toString())
//                        binding.etRelationShip2.setText( response.body()!!.single_client.contact2_relationship.toString())
//                        binding.etEmergencyMobile2.setText(response.body()!!.single_client.contact1_mobile.toString())

//                        //driver license
//                        Picasso.get()
//                            .load(response.body()!!.single_client.driver_licence_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivDl)
//
//                        //national id
//                        Picasso.get()
//                            .load(response.body()!!.single_client.national_id_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivId)
//
//                        //userphoto
//                        Picasso.get()
//                            .load(response.body()!!.single_client.user_photo_url)
//                            .fit().centerCrop()
//                            .placeholder(R.drawable.click)
//                            .error(R.drawable.click)
//                            .into(binding.ivPhoto)
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        binding.ltEditProfile.setOnClickListener{
            val intent = Intent(this@UserProfileActivity, EditProfileActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.ltUserSettings.setOnClickListener{
            val intent = Intent(this@UserProfileActivity, UserSettingsActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        binding.ltEmergency.setOnClickListener{
            val intent = Intent(this@UserProfileActivity, EmergencyContactsActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

//        binding.btSubmit.setOnClickListener {
//            val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
//            val client_id = sharedPreferences.getString("client_id", "default")
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
//            apiClient.getApiService(this).contactUpdate(client_id, contactInfo).enqueue(object : Callback<UserDetailsResponse> {
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
    }

    private fun restartApp() {
        val i = Intent(applicationContext, UserProfileActivity::class.java)
        startActivity(i)
        finish()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}