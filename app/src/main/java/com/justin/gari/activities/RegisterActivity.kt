package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityRegisterBinding
import com.justin.gari.models.userModels.signUpModel.NewUserData
import com.justin.gari.models.userModels.signUpModel.NewUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var apiClient: ApiClient
    private var theme: Switch? = null
    private lateinit var settingsManager: SettingsManager
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        }
        else
            setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        apiClient = ApiClient

        binding.btRegister.setOnClickListener {
            // display a progress dialog
            val progressDialog = ProgressDialog(this@RegisterActivity)
            progressDialog.setCancelable(false) // set cancelable to false
            progressDialog.setMessage("Creating account...") // set message
            progressDialog.show()

            val signUpInfo = NewUserData(
                binding.etFirstName.text.toString().trim(),
                binding.etLastName.text.toString().trim(),
                binding.etEmailAddress.text.toString().trim(),
                binding.etMobile.text.toString().trim(),
                binding.etCounty.text.toString().trim(),
                binding.etDistrict.text.toString().trim(),
                binding.etEstate.text.toString().trim(),
                binding.etLandMark.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )

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

        binding.tvNoAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}