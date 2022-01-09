package com.justin.gari.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val button = findViewById<TextView>(R.id.tvUpdate)
        button.setOnClickListener {
            val intent = Intent(this, MyVehiclesActivity::class.java)
            startActivity(intent)
        }
    }
}