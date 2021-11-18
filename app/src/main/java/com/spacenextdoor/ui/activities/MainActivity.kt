package com.spacenextdoor.ui.activities

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.spacenextdoor.R
import com.spacenextdoor.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    @BindView(R.id.email_image)
    lateinit var image: ImageView


    @BindView(R.id.scroll)
    lateinit var scroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        ButterKnife.bind(this)
//        this.getWindow()
//            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        setupKeyboardListener(scroll) // call in OnCreate or similar

        image.setImageResource(R.drawable.main_bg)
        changeImageOnTabClick()

        val viewPager: ViewPager = findViewById(R.id.viewPager)

        viewPager.adapter = PageAdapter(supportFragmentManager)

        tabLayout.setupWithViewPager(viewPager)

        val icons = intArrayOf(
            R.drawable.ic_message,
            R.drawable.ic_phone
        )

        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)!!.setIcon(icons[i])
        }
    }

    private fun changeImageOnTabClick() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        image.setImageResource(R.drawable.main_bg)
                    }
                    1 -> {
                        image.setImageResource(R.drawable.main_bg_phone)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }


    private fun setupKeyboardListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            if (Math.abs(view.rootView.height - (r.bottom - r.top)) > 100) { // if more than 100 pixels, its probably a keyboard...
                onKeyboardShow()
            }
        }
    }

    private fun onKeyboardShow() {
        scroll.scrollToBottomWithoutFocusChange()
    }

    fun ScrollView.scrollToBottomWithoutFocusChange() { // Kotlin extension to scrollView
        val lastChild = getChildAt(childCount - 1)
        val bottom = lastChild.bottom + paddingBottom
        val delta = bottom - (scrollY + height)
        smoothScrollBy(0, (delta - 300))
    }

}




