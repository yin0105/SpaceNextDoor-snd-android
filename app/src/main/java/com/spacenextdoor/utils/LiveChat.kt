package com.spacenextdoor.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
//import com.helpcrunch.library.core.Callback
//import com.helpcrunch.library.core.HelpCrunch
//import com.helpcrunch.library.core.options.HCOptions

import com.spacenextdoor.R

class LiveChat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        ButterKnife.bind(this)

//       HelpCrunch.initialize(
//            "spacenextdoor",
//            3,
//            "sZEtiG61xKH8brfyaOpXeJRpG882PryRJUbgltBsIqEkBAepxAikfPvMEs2yAtquYXgMX95dOw4TJj69+6AQCQ=="
//        );
//
//        val options = HCOptions.createDefault() //default or custom
//
//        HelpCrunch.showChatScreen(
//            options = options,
//            callback = object : Callback<Any?>() {
//                override fun onSuccess(result: Any?) {
//                }
//                override fun onError(message: String) {
//                }
//            }
//        )
    }
}