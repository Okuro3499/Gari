package com.justin.gari.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.api.ApiService
import com.justin.gari.models.UserLogin
import com.justin.gari.models.UserLoginResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Login Clicked", Toast.LENGTH_LONG).show()

            val email = (R.id.etEmailAddress).toString()
            val password = (R.id.etPassword).toString().trim()

            val loginInfo = UserLogin(email, password)
            val apiClient = ApiClient.buildService(ApiService::class.java)
            apiClient.loginUser(loginInfo)?.enqueue(object : Callback<UserLoginResponse?> {
                override fun onResponse(
                    call: Call<UserLoginResponse?>,
                    response: Response<UserLoginResponse?>
                ) {
                    Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG).show()
                    Log.e("Gideon", "onSuccess: $email")
                    Log.e("Gideon", "onSuccess: $response")

                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse?>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        }

        val button = findViewById<TextView>(R.id.tvNoAccount)
        button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}


