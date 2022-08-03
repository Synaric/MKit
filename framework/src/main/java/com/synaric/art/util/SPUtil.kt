package com.synaric.art.util

import android.content.Context
import com.synaric.art.BaseApplication

@Suppress("UNCHECKED_CAST", "unused")
class SPUtil {

    companion object {

        const val DEFAULT_FILE = "SYNARIC_PROJ"

        val Instance = SPUtil()
    }

    @JvmOverloads
    fun <T> getSpValue(
        key: String,
        defaultVal: T,
        filename: String = DEFAULT_FILE,
    ): T {
        val sp = BaseApplication.Instance.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return when (defaultVal) {
            is Boolean -> sp.getBoolean(key, defaultVal) as T
            is String -> sp.getString(key, defaultVal) as T
            is Int -> sp.getInt(key, defaultVal) as T
            is Long -> sp.getLong(key, defaultVal) as T
            is Float -> sp.getFloat(key, defaultVal) as T
            is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
            else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
        }
    }

    @JvmOverloads
    fun <T> putSpValue(
        key: String,
        value: T,
        filename: String = DEFAULT_FILE,
    ) {
        val editor =
            BaseApplication.Instance.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException("Unrecognized value $value")
        }
        editor.commit()
    }

    @JvmOverloads
    fun clearSpValue(
        filename: String = DEFAULT_FILE,
    ) {
        BaseApplication.Instance.getSharedPreferences(filename, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}