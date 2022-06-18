package com.synaric.art

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {

    suspend fun <T> execute(block: () -> T): T{
        return withContext(Dispatchers.IO) {
            block.invoke()
        }
    }
}