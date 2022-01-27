package com.justin.gari.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justin.gari.R
import com.justin.gari.adapters.ViewPagerAdapter
import com.justin.gari.fragments.BookingsFragment
import com.justin.gari.fragments.SavedFragment
import kotlinx.android.synthetic.main.activity_my_vehicles.*

class MyVehiclesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicles)

        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(BookingsFragment(), "Bookings")
        adapter.addFragment(SavedFragment(), "Saved")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

//        tabs.getTabAt(0)!!.(R.string.History)
//        tabs.getTabAt(1)!!.setText(R.string.Saved)



    }
}