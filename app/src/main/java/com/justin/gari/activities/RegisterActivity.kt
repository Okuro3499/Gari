package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityRegisterBinding
import com.justin.gari.models.userModels.signUpModel.NewUserData
import com.justin.gari.models.userModels.signUpModel.NewUserResponse
import com.justin.gari.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class RegisterActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    private lateinit var apiClient: ApiClient
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref!!.loadNightModeState()}")
        if (pref!!.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        binding.btRegister.setOnClickListener {
            if (TextUtils.isEmpty(binding.etFirstName.text.toString().trim())) {
                binding.etFirstName.error = "Kindly enter first name"
            } else if (TextUtils.isEmpty(binding.etLastName.text.toString().trim())) {
                binding.etLastName.error = "Kindly enter last name"
            } else if (TextUtils.isEmpty(binding.etEmailAddress.text.toString().trim())) {
                binding.etEmailAddress.error = "Kindly enter email"
            } else if(!binding.etEmailAddress.text.toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
                binding.etEmailAddress.error = "Kindly enter a valid email!"
            } else if (TextUtils.isEmpty(binding.etMobile.text.toString().trim())) {
                binding.etMobile.error = "Kindly enter mobile number"
            }  else if (TextUtils.isEmpty(binding.etCounty.text.toString().trim())) {
                binding.etCounty.error = "Kindly enter county"
            } else if (TextUtils.isEmpty(binding.etDistrict.text.toString().trim())) {
                binding.etDistrict.error = "Kindly enter district"
            }  else if (TextUtils.isEmpty(binding.etEstate.text.toString().trim())) {
                binding.etEstate.error = "Kindly enter estate"
            }  else if (TextUtils.isEmpty(binding.etLandMark.text.toString().trim())) {
                binding.etLandMark.error = "Kindly enter landmark"
            } else if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
                binding.etPassword.error = "Kindly enter password"
            } else if (binding.etPassword.text.toString().trim()!=binding.etcPassword.text.toString().trim()) {
                binding.etcPassword.error = "Passwords do not match"
            } else {
                createAccount()
            }
        }

        binding.tvNoAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount() {
        // display a progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Creating account...") // set message
        progressDialog.show()

        val today: LocalDate = LocalDate.now()

        val signUpInfo = NewUserData(pref!!.getROLEID()?.toInt(),
            binding.etFirstName.text.toString().trim(),
            binding.etLastName.text.toString().trim(),
            binding.etEmailAddress.text.toString().trim(),
            binding.etMobile.text.toString().trim().toInt(),
            binding.etCounty.text.toString().trim(),
            binding.etDistrict.text.toString().trim(),
            binding.etEstate.text.toString().trim(),
            binding.etLandMark.text.toString().trim(),
            binding.etPassword.text.toString().trim(),
            pref!!.getROLENAME(),
            pref!!.getROLEDESCRIPTION(),
            binding.etFirstName.text.toString().trim() + " " + binding.etLastName.text.toString().trim(),
            today.toString()
        )

        Log.d("data", signUpInfo.toString())
        apiClient.getApiService(this).createUser(signUpInfo).enqueue(object : Callback<NewUserResponse> {
            override fun onResponse(call: Call<NewUserResponse>, response: Response<NewUserResponse>) {
                if (response.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_LONG).show()
                    Log.e("Gideon", "onSuccess: ${response.body()}")

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<NewUserResponse>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_LONG).show()
                Log.e("Gideon", "onFailure: ${t.message}")
            }
        })
    }
}