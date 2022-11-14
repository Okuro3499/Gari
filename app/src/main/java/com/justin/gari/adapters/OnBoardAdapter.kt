package com.justin.gari.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.justin.gari.R
import com.justin.gari.activities.onBoarding.OnBoardItem

internal class OnBoardAdapter(private val mContext: Context, items: ArrayList<OnBoardItem>) :
    PagerAdapter() {
    var onBoardItems: ArrayList<OnBoardItem>
    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(mContext).inflate(R.layout.onboard_item, container, false)
        val item: OnBoardItem = onBoardItems[position]
        val imageView = itemView.findViewById<ImageView>(R.id.iv_onboard)
        imageView.setImageResource(item.getImageID())
        val title = itemView.findViewById<TextView>(R.id.title)
        title.text = item.getTitle()
        val subTitle = itemView.findViewById<TextView>(R.id.subTitle)
        subTitle.text = item.getDescription()
        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    init {
        onBoardItems = items
    }
}
