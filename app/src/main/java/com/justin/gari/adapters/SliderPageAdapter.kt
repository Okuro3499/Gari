package com.justin.gari.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.justin.gari.R
import com.squareup.picasso.Picasso

class SliderPageAdapter internal constructor(
    private val context: Context,
    private val imageUrls: Array<String>
) :
    PagerAdapter() {
    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        Picasso.get()
            .load(imageUrls[position])
            .fit().centerCrop()
            .placeholder(R.drawable.vehicle_placeholder)
            .error(R.drawable.vehicle_placeholder)
            .into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
