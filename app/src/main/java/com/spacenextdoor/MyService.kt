package com.spacenextdoor

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.spacenextdoor.utils.PrefManger

class MyService : Service() {

    private val TAG = "ServiceExample"

    override fun onCreate() {
        Log.e(TAG, "Service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(TAG, "Service onStartCommand " + startId)


        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.e(TAG, "Service onBind")
        return null
    }

    override fun onDestroy() {
        Log.e(TAG, "Service onDestroy")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e(TAG, "Service onDestroy")
        println("onTaskRemoved called")
        PrefManger.clearPreferencesData(this)
        stopSelf()
        super.onTaskRemoved(rootIntent)
        //do something you want
        //stop service
        this.stopSelf()
    }
}