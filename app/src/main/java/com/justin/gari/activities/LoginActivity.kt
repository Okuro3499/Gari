package com.justin.gari.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.api.SessionManager
import com.justin.gari.models.userModels.loginModel.UserLogin
import com.justin.gari.models.userModels.loginModel.UserLoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiClient = ApiClient
        sessionManager = SessionManager(this)

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        //Login into user account
        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmailAddress).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()

            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            val loginInfo = UserLogin(email, password)
            apiClient.getApiService(this).loginUser(loginInfo)
                .enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(
                        call: Call<UserLoginResponse>, response: Response<UserLoginResponse>
                    ) {
                        Log.e("Gideon", "onSuccess: $response")
                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successful",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            Log.e("Gideon", "onSuccess: ${response.body()}")
                            editor.putString("client_id", response.body()!!.user.client_id)
                            editor.putString("first_name", response.body()!!.user.first_name)
                            editor.putString("last_name", response.body()!!.user.last_name)
                            editor.putString("email", response.body()!!.user.email)
                            editor.apply()

                            response.body()!!.accessToken?.let { it1 ->
                                sessionManager.saveAuthToken(
                                    it1
                                )
                            }

                            val intent =
                                Intent(this@LoginActivity, MyVehiclesActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_LONG).show()
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
    }
}


