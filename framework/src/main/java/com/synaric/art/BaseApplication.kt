package com.synaric.art

import android.app.Application

class BaseApplication: Application() {

    companion object {
        lateinit var Instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this
    }
}