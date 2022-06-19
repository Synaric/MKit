package com.synaric.mkit.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class StringUtil {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun dateStrToDate(dateStr: String): Date {
            val pattern = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            return simpleDateFormat.parse(dateStr) ?: Date(0L)
        }
    }
}