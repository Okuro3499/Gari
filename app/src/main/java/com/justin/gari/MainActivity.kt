package com.justin.gari

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val arrayList = ArrayList<carModel>()
        arrayList.add(
            carModel(
                R.drawable.premio,
                "Toyota Premio",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.lexus,
                "Lexus",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.note,
                "Nissan Note",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.passo,
                "Toyota Passo",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.rumion,
                "Toyota Rumion",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.wish,
                "Toyota Wish",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.prado,
                "Toyota Prado",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.axio,
                "Toyota Axio",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.demio,
                "Mazda Demio",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )
        arrayList.add(
            carModel(
                R.drawable.fit,
                "Honda Fit",
                "Self Drive/Chauffered",
                "Automatic",
                "3500 per day",
                "Available"
            )
        )

        val adapter = carAdapter(arrayList, this)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter


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
}