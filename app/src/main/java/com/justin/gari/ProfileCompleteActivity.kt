package com.justin.gari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProfileCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_complete)

        val button = findViewById<Button>(R.id.btSubmit)
        button.setOnClickListener{
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}