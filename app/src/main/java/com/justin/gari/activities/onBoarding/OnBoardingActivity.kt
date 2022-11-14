package com.justin.gari.activities.onBoarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.justin.gari.R
import com.justin.gari.activities.MainActivity
import com.justin.gari.adapters.OnBoardAdapter
import com.justin.gari.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    private val sharedPrefFile = "sharedPrefData"
    private lateinit var binding: ActivityOnBoardingBinding
    private var dotsCount = 0
    private var dots: Array<ImageView?>? = null
    private var mAdapter: OnBoardAdapter? = null
    var previousPos = 0

    private var onBoardItems = ArrayList<OnBoardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("onBoarding", false)) {
            // Start Main Activity
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)

            // Close Onboarding
            finish()
            return
        }

        mAdapter = OnBoardAdapter(this, onBoardItems)
        binding.pagerIntroduction.adapter = mAdapter
        binding.pagerIntroduction.currentItem = 0
        binding.pagerIntroduction.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                // Change the current position intimation
                for (i in 0 until dotsCount) {
                    dots!![i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@OnBoardingActivity,
                            R.drawable.nonselecteddark
                        )
                    )
                }
                mAdapter!!.notifyDataSetChanged()

                dots!![position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OnBoardingActivity,
                        R.drawable.selecteddark
                    )
                )

                val pos = position + 1

                if (pos == dotsCount && previousPos == dotsCount - 1)
                    showAnimation()
                else if (pos == dotsCount - 1 && previousPos == dotsCount)
                    hideAnimation()


                previousPos = pos
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        binding.getStarted.setOnClickListener { finishTutorial() }

        loadData()
        setUiPageViewController()
    }

    private fun loadData() {
        val header = intArrayOf(R.string.header1, R.string.header2, R.string.header3)
        val desc = intArrayOf(R.string.subHeader1, R.string.subHeader2, R.string.subHeader3)
        val imageId = intArrayOf(
            R.drawable.on_board_1,
            R.drawable.on_board_2,
            R.drawable.on_board_3
        )
        for (i in imageId.indices) {
            val item = OnBoardItem()
            item.setImageID(imageId[i])
            item.setTitle(resources.getString(header[i]))
            item.setDescription(resources.getString(desc[i]))
            onBoardItems.add(item)
        }
        mAdapter!!.notifyDataSetChanged()
    }

    // Button bottomUp animation
    fun showAnimation() {
        val show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim)
        binding.getStarted.startAnimation(show)
        show.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.getStarted.visibility = View.VISIBLE
                binding.skip.visibility = View.GONE
//                binding.background.setBackgroundResource(R.color.colorPrimary)
//                binding.title.resources.getColor(R.color.md_white_1000)
//                binding.subTitle.resources.getColor(R.color.md_white_1000)
//                binding.viewPagerCountDots.visibility = View.GONE
                binding.nextPage.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.getStarted.clearAnimation()
            }
        })
    }

    // Button Topdown animation
    fun hideAnimation() {
        val hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim)
        binding.getStarted.startAnimation(hide)

        hide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.getStarted.clearAnimation()
                binding.getStarted.visibility = View.GONE
//                binding.background.setBackgroundResource(R.color.colorPrimary)
//                binding.title.resources.getColor(R.color.md_white_1000)
//                binding.subTitle.resources.getColor(R.color.md_white_1000)
                binding.skip.visibility = View.VISIBLE
                binding.viewPagerCountDots.visibility = View.VISIBLE
            }
        })
    }

    private fun setUiPageViewController() {
        dotsCount = mAdapter!!.count
        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(this)
            dots!![i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this@OnBoardingActivity,
                    R.drawable.nonselecteddark
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(6, 0, 6, 0)
            binding.viewPagerCountDots.addView(dots!![i], params)
        }
        mAdapter!!.notifyDataSetChanged()
        dots!![0]?.setImageDrawable(
            ContextCompat.getDrawable(
                this@OnBoardingActivity,
                R.drawable.selecteddark
            )
        )
    }

    private fun finishTutorial() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("onBoarding", true)
        editor.apply()
        // Set onboarding_complete to true
//        pref.setON_BOARDING(true)

//         Launch the main Activity, called MainActivity
        val main = Intent(this, MainActivity::class.java)
        startActivity(main)

        // Close the OnboardingActivity
        finish()
    }
}