package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.utils.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.api.SessionManager
import com.justin.gari.databinding.ActivityLoginBinding
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import com.justin.gari.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var settingsManager: SettingsManager
    var theme: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        apiClient = ApiClient
        sessionManager = SessionManager(this)
        pref = SharedPrefManager(this)

        if(pref!!.getSWITCHEDTHEME()){
            settingsManager.setNightModeState(true)
        } else if(!pref!!.getSWITCHEDTHEME()){
            settingsManager.setNightModeState(false)
        }


        //Login into user account
        binding.btLogin.setOnClickListener {
            if (TextUtils.isEmpty(binding.etEmailAddress.text.toString().trim())) {
                binding.etEmailAddress.error = "Kindly enter email"
            } else if(!binding.etEmailAddress.text.toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
                binding.etEmailAddress.error = "Kindly enter a valid email!"
            } else if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
                binding.etPassword.error = "Kindly enter password"
            } else {
                // display a progress dialog
                val progressDialog = ProgressDialog(this@LoginActivity)
                progressDialog.setCancelable(false) // set cancelable to false
                progressDialog.setMessage("Logging in...") // set message
                progressDialog.show()

                val loginInfo = UserLogin(
                    binding.etEmailAddress.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
                apiClient.getApiService(this).loginUser(loginInfo).enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                        if (response.isSuccessful) {
                            progressDialog.dismiss()
                            Snackbar.make(it, "Login Successful", Snackbar.LENGTH_SHORT).show()
                            Log.e("Gideon", "onSuccess: ${response.body()}")

                            pref!!.setUSERID(response.body()!!.users.user_id)
                            pref!!.setROLEID(response.body()!!.users.role_id)
                            pref!!.setFIRSTNAME(response.body()!!.users.first_name)
                            pref!!.setLASTNAME(response.body()!!.users.last_name)
                            pref!!.setEMAIL(response.body()!!.users.email)
                            pref!!.setUSERPROFILEPHOTO(response.body()!!.users.user_photo_url)
                        response.body()!!.accessToken?.let { it1 -> sessionManager.saveAuthToken(it1) }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    progressDialog.dismiss()
                    Snackbar.make(it, "${t.message}", Snackbar.LENGTH_SHORT).show()
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
                })
            }
        }

        //Go to register activity if not registered
        binding.tvNoAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
        exitProcess(0)
    }
}


