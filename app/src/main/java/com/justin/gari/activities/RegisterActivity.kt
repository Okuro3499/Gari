package com.justin.gari.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.api.ApiService
import com.justin.gari.models.NewUserResponse
import com.justin.gari.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton = findViewById<Button>(R.id.btRegister)
        registerButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmailAddress).text.toString().trim()
            val firstName = findViewById<EditText>(R.id.etFirstName).text.toString().trim()
            val lastName = findViewById<EditText>(R.id.etLastName).text.toString().trim()
            val mobile = findViewById<EditText>(R.id.etMobile).text.toString().trim()
            val county = findViewById<EditText>(R.id.etCounty).text.toString().trim()
            val district = findViewById<EditText>(R.id.etDistrict).text.toString().trim()
            val estate = findViewById<EditText>(R.id.etEstate).text.toString().trim()
            val landmark = findViewById<EditText>(R.id.etLandMark).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()

            Toast.makeText(
                this@RegisterActivity,
                "Registration clicked",
                Toast.LENGTH_LONG
            ).show()

            val signUpInfo = User(email, firstName, lastName, mobile, county, district, estate, landmark, password)
            val apiClient = ApiClient.buildService(ApiService::class.java)
            apiClient.createUser(signUpInfo).enqueue(object : Callback<NewUserResponse> {

                override fun onResponse(
                    call: Call<NewUserResponse>, response: Response<NewUserResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration Successful",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<NewUserResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_LONG)
                        .show()
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })
        }

//        val button = findViewById<TextView>(R.id.btRegister)
//        button.setOnClickListener{
//            val intent = Intent(this, ProfileCompleteActivity::class.java)
//            startActivity(intent)
//        }
    }
}