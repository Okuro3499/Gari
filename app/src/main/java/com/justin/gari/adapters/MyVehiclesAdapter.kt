package com.justin.gari.adapters

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


//class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {
//    private val mFragmentList = ArrayList<Fragment>()
//    private val mFragmentTitleList = ArrayList<String>()
//
//    override fun getCount(): Int {
//        return mFragmentList.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return mFragmentList[position]
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return mFragmentTitleList[position]
//    }
//
//    fun addFragment(fragment: Fragment, title: String) {
//        mFragmentList.add(fragment)
//        mFragmentTitleList.add(title)
//    }
//}

class MyVehiclesAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    private val fragmentArrayList: ArrayList<Fragment> = ArrayList()
    private val fragmentTitle: ArrayList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentArrayList.add(fragment)
        fragmentTitle.add(title)
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }
}