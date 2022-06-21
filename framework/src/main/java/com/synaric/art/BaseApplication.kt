package com.synaric.art

import android.app.Application

class BaseApplication: Application() {

    companion object {
        lateinit var INSTANCE: BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}