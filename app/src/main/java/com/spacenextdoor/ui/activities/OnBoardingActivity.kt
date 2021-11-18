package com.spacenextdoor.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import butterknife.ButterKnife
import butterknife.OnClick
import com.spacenextdoor.R
import com.spacenextdoor.adapter.SliderAdapter
import com.spacenextdoor.model.UserInfoModel
import com.spacenextdoor.utils.PrefManger
import kotlinx.android.synthetic.main.activity_on_boarding.*
import me.relex.circleindicator.CircleIndicator3

class OnBoardingActivity : AppCompatActivity() {


    private var desc = mutableListOf<String>()
    private var titleList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()
    lateinit var pageDetail: SharedPreferences

    lateinit var adapters: SliderAdapter

    val welcomeScreenShownPref = "welcomeScreenShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        ButterKnife.bind(this)

        val userData: UserInfoModel? = PrefManger.getUser(this)

        pageDetail = getSharedPreferences("welcomeScreen", Context.MODE_PRIVATE)

        determineNextActivity(userData)

    }

    private fun determineNextActivity(userData: UserInfoModel?) {

        if (!pageDetail.getBoolean(welcomeScreenShownPref, true) && userData == null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else if (userData != null && !pageDetail.getBoolean(welcomeScreenShownPref, false)) {
            goToLogin()
        } else {
            slideBoardingImages()
        }
    }


    private fun goToLogin() {

        val userVal: UserInfoModel? = PrefManger.getUser(this)

        if (userVal == null) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val editor = pageDetail.edit()
            editor.putBoolean(welcomeScreenShownPref, false)
            editor.apply()
            finish()

        } else {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intent)

        }
    }

    private fun slideBoardingImages() {

        imageList.add(R.drawable.ic_onboarding_screen1)
        imageList.add(R.drawable.ic_onboarding_screen2)
        imageList.add(R.drawable.ic_onboarding_screen3)
        imageList.add(R.drawable.ic_onboarding_screen4)

        titleList.add(getString(R.string.label_one))
        titleList.add(getString(R.string.label_two))
        titleList.add(getString(R.string.label_three))
        titleList.add(getString(R.string.label_four))

        desc.add(getString(R.string.label_five))
        desc.add(getString(R.string.label_six))
        desc.add(getString(R.string.label_seven))
        desc.add(getString(R.string.label_eight))

        settingAdapterData(imageList, titleList, desc)

    }

    private fun settingAdapterData(
        imageList: MutableList<Int>,
        titleList: MutableList<String>,
        desc: MutableList<String>
    ) {

        adapters = SliderAdapter(this)
        adapters.setContentList(imageList, titleList, desc)
        imageSlider.adapter = adapters

        val indicator = findViewById<CircleIndicator3>(R.id.tabDots)
        indicator.setViewPager(imageSlider)

        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                println(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                println(position)
            }

            override fun onPageSelected(position: Int) {
                if (position >= 1) {
                    back_img.visibility = View.VISIBLE
                } else {
                    back_img.visibility = View.INVISIBLE
                }
                if (position >= 3) {
                    // show the btn
                    btnGetStarted.visibility = View.VISIBLE
                } else {
                    //hide the button
                    btnGetStarted.visibility = View.INVISIBLE
                }
                super.onPageSelected(position)
                println(position)
            }
        })

    }

    //Click Listeners

    @OnClick(R.id.close_img)
    fun closeClick() {
        goToLogin()
    }

    @OnClick(R.id.back_img)
    fun backClick() {
        imageSlider.setCurrentItem(imageSlider.currentItem - 1, true);
    }

    @OnClick(R.id.btnGetStarted)
    fun getStarted() {
        goToLogin()
    }
}