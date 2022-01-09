package com.justin.gari.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.justin.gari.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val button = findViewById<TextView>(R.id.btRegister)
        button.setOnClickListener{
            val intent = Intent(this, ProfileCompleteActivity::class.java)
            startActivity(intent)
        }
    }
}