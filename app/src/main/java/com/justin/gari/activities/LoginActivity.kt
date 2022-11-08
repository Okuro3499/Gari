package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.api.SessionManager
import com.justin.gari.databinding.ActivityLoginBinding
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityLoginBinding
    private lateinit var settingsManager: SettingsManager

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
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

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

                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                val loginInfo = UserLogin(
                    binding.etEmailAddress.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
                apiClient.getApiService(this).loginUser(loginInfo).enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                            if (response.isSuccessful) {
                                progressDialog.dismiss()
                                Snackbar.make(it, "Login Successful", Snackbar.LENGTH_SHORT).show()
//                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()
                                Log.e("Gideon", "onSuccess: ${response.body()}")
                                editor.putString("client_id", response.body()!!.user.client_id)
                                editor.putString("first_name", response.body()!!.user.first_name)
                                editor.putString("last_name", response.body()!!.user.last_name)
                                editor.putString("email", response.body()!!.user.email)
                                editor.putString(
                                    "userProfile",
                                    response.body()!!.user.user_photo_url
                                )
                                editor.apply()

                                response.body()!!.accessToken?.let { it1 ->
                                    sessionManager.saveAuthToken(
                                        it1
                                    )
                                }

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            }
                        }

                        override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                            progressDialog.dismiss()
                            Snackbar.make(it, "${t.message}", Snackbar.LENGTH_SHORT).show()
//                    Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_LONG).show()
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
}


