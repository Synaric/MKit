@file:Suppress("unused")

package com.synaric.mkit.util

import android.util.Log

class AppLog {

    companion object {

        fun d(from: Any, msg: String?) {
            log(from, msg, Log.DEBUG)
        }

        fun w(from: Any, msg: String?) {
            log(from, msg, Log.WARN)
        }

        fun e(from: Any, msg: String?) {
            log(from, msg, Log.ERROR)
        }

        private fun log(from: Any, msg: String?, level: Int) {
            when(level) {
                Log.DEBUG -> {
                    Log.d(from.javaClass.name, msg.toString())
                }
                Log.WARN -> {
                    Log.w(from.javaClass.name, msg.toString())
                }
                Log.ERROR -> {
                    Log.e(from.javaClass.name, msg.toString())
                }
            }
        }
    }
}