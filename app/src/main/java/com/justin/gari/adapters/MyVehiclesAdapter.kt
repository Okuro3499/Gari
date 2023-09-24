package com.justin.gari.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.justin.gari.fragments.BookingsFragment

class MyVehiclesAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            BookingsFragment()
        } else BookingsFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}

//class MyVehiclesAdapter(fm: FragmentManager, behavior: Int) :
//    FragmentPagerAdapter(fm, behavior) {
//    private val fragmentArrayList: ArrayList<Fragment> = ArrayList()
//    private val fragmentTitle: ArrayList<String> = ArrayList()
//    override fun getItem(position: Int): Fragment {
//        return fragmentArrayList[position]
//    }
//
//    override fun getCount(): Int {
//        return fragmentArrayList.size
//    }
//
//    fun addFragment(fragment: Fragment, title: String) {
//        fragmentArrayList.add(fragment)
//        fragmentTitle.add(title)
//    }
//
//    @Nullable
//    override fun getPageTitle(position: Int): CharSequence? {
//        return fragmentTitle[position]
//    }
//}