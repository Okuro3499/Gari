package com.justin.gari.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.justin.gari.fragments.HistoryFragment
import com.justin.gari.R
import com.justin.gari.fragments.SavedFragment
import com.justin.gari.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_my_vehicles.*

class MyVehiclesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicles)
        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HistoryFragment(), "History")
        adapter.addFragment(SavedFragment(), "Saved")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setText(R.string.History)
        tabs.getTabAt(1)!!.setText(R.string.Saved)
    }
}