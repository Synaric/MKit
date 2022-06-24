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
            val tag: String = when (from) {
                is String -> {
                    from
                }
                else -> {
                    from.javaClass.name
                }
            }
            when (level) {
                Log.DEBUG -> {
                    Log.d(tag, msg.toString())
                }
                Log.WARN -> {
                    Log.w(tag, msg.toString())
                }
                Log.ERROR -> {
                    Log.e(tag, msg.toString())
                }
            }
        }
    }
}