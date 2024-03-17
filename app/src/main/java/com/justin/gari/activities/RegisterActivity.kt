package com.justin.gari.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.databinding.ActivityRegisterBinding
import com.justin.gari.databinding.DialogMessageBinding
import com.justin.gari.models.ApiErrorResponse
import com.justin.gari.models.userModels.signUpModel.NewUserData
import com.justin.gari.models.userModels.signUpModel.NewUserResponse
import com.justin.gari.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    var pref: SharedPrefManager? = null
    private lateinit var apiClient: ApiClient
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        apiClient = ApiClient()
        pref = SharedPrefManager(this)
        Log.d("NightModeState", "${pref?.loadNightModeState()}")
        if (pref?.loadNightModeState() == true) {
            setTheme(R.style.DarkGari)
        } else setTheme(R.style.Gari)

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        binding.btRegister.setOnClickListener {
            if (TextUtils.isEmpty("${binding.etFirstName.text}".trim())) {
                binding.etFirstName.error = getString(R.string.enter_first_name)
            } else if (TextUtils.isEmpty("${binding.etLastName.text}".trim())) {
                binding.etLastName.error = getString(R.string.enter_last_name)
            } else if (TextUtils.isEmpty("${binding.etEmailAddress.text}".trim())) {
                binding.etEmailAddress.error = getString(R.string.enter_email)
            } else if(!"${binding.etEmailAddress.text}".trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())) {
                binding.etEmailAddress.error = getString(R.string.enter_a_valid_email)
            } else if (TextUtils.isEmpty("${binding.etMobile.text}".trim())) {
                binding.etMobile.error = getString(R.string.enter_mobile_number)
            } else if(!"${binding.etMobile.text}".trim().matches("^((\\+254|254|0)[0-9]{9})$".toRegex())) {
                binding.etMobile.error = getString(R.string.enter_a_valid_phone_no)
            }
//            else if (TextUtils.isEmpty("${binding.etCounty.text}".trim())) {
//                binding.etCounty.error = "Kindly enter county"
//            } else if (TextUtils.isEmpty("${binding.etDistrict.text}".trim())) {
//                binding.etDistrict.error = "Kindly enter district"
//            }  else if (TextUtils.isEmpty("${binding.etEstate.text}".trim())) {
//                binding.etEstate.error = "Kindly enter estate"
//            }  else if (TextUtils.isEmpty("${binding.etLandMark.text}".trim())) {
//                binding.etLandMark.error = "Kindly enter landmark"
//            }
            else if (TextUtils.isEmpty("${binding.etPassword.text}".trim())) {
                binding.etPassword.error = getString(R.string.enter_password)
            } else if ("${binding.etPassword.text}".trim()!= "${binding.etcPassword.text}".trim()) {
                binding.etcPassword.error = getString(R.string.passwords_do_not_match)
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
        progressDialog.setMessage(getString(R.string.creating_account)) // set message
        progressDialog.show()

        val phoneNumber = "${binding.etMobile.text}".trim().takeLast(9)

        val signUpInfo = NewUserData(
            2,
            "${binding.etFirstName.text}".trim(),
            "${binding.etLastName.text}".trim(),
            "${binding.etEmailAddress.text}".trim(),
            phoneNumber,
//            "${binding.etCounty.text}".trim(),
//            "${binding.etDistrict.text}".trim(),
//            "${binding.etEstate.text}".trim(),
//            "${binding.etLandMark.text}".trim(),
            "${binding.etPassword.text}".trim(),
            1
        )

        Log.d("data", signUpInfo.toString())
        apiClient.getApiService().createUser(signUpInfo).enqueue(object : Callback<NewUserResponse> {
            override fun onResponse(call: Call<NewUserResponse>, response: Response<NewUserResponse>) {
                if (!response.isSuccessful) {
                    val gson = Gson()
                    val errorBody = response.errorBody()?.string()
                    try {
                        val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
                        progressDialog.dismiss()
                        val dialog = Dialog(this@RegisterActivity)
                        dialog.setCancelable(false)

                        val dialogMessageBinding = DialogMessageBinding.inflate(layoutInflater)
                        dialog.setContentView(dialogMessageBinding.root)
                        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                        dialogMessageBinding.messageTitle.text = errorResponse.status ?: getString(R.string.failed_to_register_user)
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
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.e("Gideon", "onSuccess: ${response.body()}")

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<NewUserResponse>, t: Throwable) {
                progressDialog.dismiss()
                val dialog = Dialog(this@RegisterActivity)
                dialog.setCancelable(false)

                val dialogMessageBinding = DialogMessageBinding.inflate(layoutInflater)
                dialog.setContentView(dialogMessageBinding.root)
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                dialogMessageBinding.messageTitle.text = getString(R.string.failed_to_register_user)
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