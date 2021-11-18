package com.spacenextdoor.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.spacenextdoor.ui.fragments.EmailLoginFragment
import com.spacenextdoor.ui.fragments.PhoneLoginFragment


class PageAdapter(fm : FragmentManager) : FragmentPagerAdapter (fm) {
    override fun getCount(): Int {
        return 2
    }
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
               EmailLoginFragment()
            }
            1 -> {
                PhoneLoginFragment()
            }
            else -> {
                EmailLoginFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {return "EMAIL" }
            1 -> {return "PHONE NUMBER"}
        }
        return super.getPageTitle(position)
    }
}