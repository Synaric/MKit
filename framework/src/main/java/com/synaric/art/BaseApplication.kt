package com.synaric.art

import android.app.Application

class BaseApplication: Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}