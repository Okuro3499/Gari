package com.justin.gari.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.justin.gari.R
import com.justin.gari.SettingsManager
import com.justin.gari.api.ApiClient
import com.justin.gari.api.SessionManager
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private var showPass = false
    private var theme: Switch? = null
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsManager = SettingsManager(this)
        if (settingsManager.loadNightModeState()==true){
            setTheme(R.style.DarkGari)
        } else
            setTheme(R.style.Gari)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient
        sessionManager = SessionManager(this)
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        //Login into user account
        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener {
            // display a progress dialog
            val progressDialog = ProgressDialog(this@LoginActivity)
            progressDialog.setCancelable(false) // set cancelable to false
            progressDialog.setMessage("Logging in...") // set message
            progressDialog.show()

            val email = findViewById<EditText>(R.id.etEmailAddress).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            val loginInfo = UserLogin(email, password)
            apiClient.getApiService(this).loginUser(loginInfo).enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse> ) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        Snackbar.make(it, "Login Successful", Snackbar.LENGTH_SHORT).show()
//                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        editor.putString("client_id", response.body()!!.user.client_id)
                        editor.putString("first_name", response.body()!!.user.first_name)
                        editor.putString("last_name", response.body()!!.user.last_name)
                        editor.putString("email", response.body()!!.user.email)
                        editor.apply()

                        response.body()!!.accessToken?.let { it1 ->
                            sessionManager.saveAuthToken(it1)
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
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

        //Go to register activity if not registered
        val button = findViewById<TextView>(R.id.tvNoAccount)
        button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val passToggle = findViewById<ImageView>(R.id.password_toggle)
        passToggle.setOnClickListener{
            showPass = !showPass
            showPassword(showPass)
        }
        showPassword(showPass)
    }

    private fun showPassword(isShow: Boolean){
        val password = findViewById<EditText>(R.id.etPassword)
        val passToggle = findViewById<ImageView>(R.id.password_toggle)
        if (isShow) {
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passToggle.setImageResource(R.drawable.outline_visibility_off_black_24dp)
        }else {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            passToggle.setImageResource(R.drawable.outline_visibility_black_24dp)
        }
        password.setSelection(password.text.toString().length)
    }
}


