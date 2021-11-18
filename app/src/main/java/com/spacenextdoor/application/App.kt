package com.spacenextdoor.application

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import com.helpcrunch.library.core.HelpCrunch
import com.helpcrunch.library.core.models.user.HCUser
import com.spacenextdoor.utils.PrefManger


class App : MultiDexApplication() {
    var ORGANIZATION: String = "spacenextdoor"
    var APP_ID: Int = 3
    var SECRET: String =
        "sZEtiG61xKH8brfyaOpXeJRpG882PryRJUbgltBsIqEkBAepxAikfPvMEs2yAtquYXgMX95dOw4TJj69+6AQCQ=="

    fun getMyUser(): HCUser {
        return HCUser.Builder()
            .withName("")
            .withEmail("")
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        HelpCrunch.initialize(ORGANIZATION, APP_ID, SECRET, getMyUser())
        ProcessLifecycleOwner.get().getLifecycle().addObserver(AppLifecycleListener())
    }


    public class AppLifecycleListener : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public fun onMoveToForeground() {
            Log.e("TAG", "For ground")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public fun onMoveToBackground() {
            // clearState()
            Log.e("TAG", "back ground")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public fun onMoveToDestory() {
            //  clearState()
            Log.e("TAG", "back destory")
        }
    }
}