package com.justin.gari.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.api.SessionManager
import com.justin.gari.databinding.ActivityLoginBinding
import com.justin.gari.databinding.DialogMessageBinding
import com.justin.gari.models.ApiErrorResponse
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import com.justin.gari.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    lateinit var pref: SharedPrefManager
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref.loadNightModeState()}")
        if (pref.loadNightModeState()) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        sessionManager = SessionManager(this)

        //Login into user account
        binding.btLogin.setOnClickListener { it1 ->
            if (TextUtils.isEmpty("${binding.etPhoneNo.text}".trim())) {
                binding.etPhoneNo.error = getString(R.string.enter_mobile_number)
            } else if(!"${binding.etPhoneNo.text}".trim().matches("^((\\+254|254|0)[0-9]{9})$".toRegex())) {
                binding.etPhoneNo.error = getString(R.string.enter_a_valid_phone_no)
            } else if (TextUtils.isEmpty("${binding.etPassword.text}".trim())) {
                binding.etPassword.error = getString(R.string.enter_password)
            } else {
                // display a progress dialog
                val progressDialog = ProgressDialog(this@LoginActivity)
                progressDialog.setCancelable(false) // set cancelable to false
                progressDialog.setMessage(getString(R.string.logging_in)) // set message
                progressDialog.show()
                val phoneNumber = "${binding.etPhoneNo.text}".trim().takeLast(9)

                val loginInfo = UserLogin(
                    phoneNumber,
                    "${binding.etPassword.text}".trim()
                )
                apiClient.getApiService().loginUser(loginInfo).enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                        if (!response.isSuccessful) {
                            val gson = Gson()
                            val errorBody = response.errorBody()?.string()
                            try {
                                val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                                progressDialog.dismiss()
                                val dialog = Dialog(this@LoginActivity)
                                dialog.setCancelable(false)

                                val dialogMessageBinding = DialogMessageBinding.inflate(layoutInflater)
                                dialog.setContentView(dialogMessageBinding.root)
                                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                                dialogMessageBinding.messageTitle.text = errorResponse.status ?: getString(R.string.failed_to_login)
                                dialogMessageBinding.message.text = errorResponse.error ?: getString(R.string.kindly_contact_admin)
                                dialogMessageBinding.btBack.setOnClickListener {
                                    dialog.dismiss()
                                }

                                dialog.setCancelable(true)
                                dialog.show()
                            } catch (e: Exception) {
                                Log.e("Error Parsing", "Error parsing error response", e)
                            }
                        } else {
                            progressDialog.dismiss()

                            Log.e("Gideon", "onSuccess: ${response.body()}")
                            response.body()?.let {
                                Snackbar.make(it1, "${it.status}", Snackbar.LENGTH_SHORT).show()
                                pref.setUSERID(it.user?.user_id)
                                pref.setROLEID(it.user?.role_id)
                                pref.setFIRSTNAME(it.user?.first_name)
                                pref.setLASTNAME(it.user?.last_name)
                                pref.setEMAIL(it.user?.email)
                                pref.setUSERPROFILEPHOTO(it.user?.user_photo_url)
                                sessionManager.saveAuthToken(it.accessToken)
                            }

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        progressDialog.dismiss()
                        val dialog = Dialog(this@LoginActivity)
                        dialog.setCancelable(false)

                        val dialogMessageBinding = DialogMessageBinding.inflate(layoutInflater)
                        dialog.setContentView(dialogMessageBinding.root)
                        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                        dialogMessageBinding.messageTitle.text = getString(R.string.failed_to_login)
                        dialogMessageBinding.message.text = "${t.message}"
                        dialogMessageBinding.btBack.setOnClickListener {
                            dialog.dismiss()
                        }

                        dialog.setCancelable(true)
                        dialog.show()
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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
                exitProcess(0)
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}


