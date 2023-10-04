package com.bmprj.cointicker

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(){

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            handleException(throwable)
        }
    }

    private fun handleException(throwable: Throwable){
        Log.e(applicationContext.javaClass.simpleName,throwable.message.toString())
    }
}