package com.synaric.art

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository() {

    val context: Context

    init {
        context = BaseApplication.Instance
    }

    suspend fun <T> execute(block: () -> T): T{
        return withContext(Dispatchers.IO) {
            block.invoke()
        }
    }
}