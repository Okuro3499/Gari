package com.justin.gari.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.api.ApiClient
import com.justin.gari.models.userModels.UserDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileCompleteActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_complete)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val firstName = findViewById<TextView>(R.id.tvFirstName)
        val lastName = findViewById<TextView>(R.id.tvLastName)
        val email = findViewById<TextView>(R.id.tvEmail)
        val mobile = findViewById<TextView>(R.id.tvMobile)
        val county = findViewById<TextView>(R.id.tvCounty)
        val district = findViewById<TextView>(R.id.tvDistrict)
        val estate = findViewById<TextView>(R.id.tvEstate)
        val landMark = findViewById<TextView>(R.id.tvLandMark)

        val clientId = sharedPreferences.getString("client_id", "default")

        apiClient = ApiClient
        apiClient.getApiService(this).getUserDetails(clientId)
            .enqueue(object : Callback<UserDetailsResponse> {
                override fun onResponse(
                    call: Call<UserDetailsResponse>,
                    response: Response<UserDetailsResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("Gideon", "onSuccess: ${response.body()}")
                        firstName.text = response.body()!!.single_client.first_name.toString()
                        lastName.text = response.body()!!.single_client.last_name.toString()
                        email.text = response.body()!!.single_client.email.toString()
                        mobile.text = response.body()!!.single_client.mobile.toString()
                        county.text = response.body()!!.single_client.county.toString()
                        district.text = response.body()!!.single_client.district.toString()
                        estate.text = response.body()!!.single_client.estate.toString()
                        landMark.text = response.body()!!.single_client.landmark.toString()
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                    Log.e("Gideon", "onFailure: ${t.message}")
                }
            })

        val button = findViewById<Button>(R.id.btSubmit)
        button.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}