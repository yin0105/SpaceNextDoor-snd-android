package com.spacenextdoor.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.spacenextdoor.R
import com.spacenextdoor.model.UserInfoModel
import com.spacenextdoor.utils.Constant
import com.spacenextdoor.utils.PrefManger

class SplashActivity : AppCompatActivity() {

    lateinit var pageDetail: SharedPreferences

    val welcomeScreenShownPref = "welcomeScreenShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)

//        Log.e("show constant" , Constant.appId)

        val updateHandler = Handler()

        val runnable = Runnable {
            checkConditions()
        }
        updateHandler.postDelayed(runnable, 500)

    }

    private fun checkConditions() {

        val userData: UserInfoModel? = PrefManger.getUser(this)
        pageDetail = getSharedPreferences("welcomeScreen", Context.MODE_PRIVATE)
        determineNextActivity(userData)
    }

    private fun determineNextActivity(userData: UserInfoModel?) {

        if (!pageDetail.getBoolean(welcomeScreenShownPref, true) && userData == null) {
            goToOnBoardingActivity()
        } else if (userData != null && !pageDetail.getBoolean(welcomeScreenShownPref, false)) {
            goToLogin()
        } else {
            goToOnBoardingActivity()
        }
    }

    private fun goToOnBoardingActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToLogin() {

        val userVal: UserInfoModel? = PrefManger.getUser(this)

        if (userVal == null) {

            val editor = pageDetail.edit()
            editor.putBoolean(welcomeScreenShownPref, false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}