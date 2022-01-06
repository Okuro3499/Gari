package com.justin.gari

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

import kotlin.collections.ArrayList

class DetailActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val button = findViewById<Button>(R.id.btBook)
        button.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        pickDate()

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel(R.drawable.premio))
        imageList.add(SlideModel(R.drawable.rear))
        imageList.add(SlideModel(R.drawable.seats))
        imageList.add(SlideModel(R.drawable.dash))
        imageList.add(SlideModel(R.drawable.speedometer))
        imageList.add(SlideModel(R.drawable.pre))

        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT)
                    .show()
                R.id.profile -> Toast.makeText(
                    applicationContext,
                    "Clicked Profile",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.myVehicles -> Toast.makeText(
                    applicationContext,
                    "Clicked My Vehicles",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.logout -> Toast.makeText(
                    applicationContext,
                    "Clicked Logout",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.about -> Toast.makeText(
                    applicationContext,
                    "Clicked About",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.help -> Toast.makeText(applicationContext, "Clicked Help", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)

    }

    private fun pickDate() {
        ETDFrom.setOnClickListener{
            getDateTimeCalendar()

            DatePickerDialog(this, this, year,month,day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
    }
}