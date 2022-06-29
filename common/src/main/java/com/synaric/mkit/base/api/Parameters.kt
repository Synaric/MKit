package com.synaric.mkit.base.api

import androidx.paging.PagingConfig

interface Parameters<T : Any> {
    val pagingConfig: PagingConfig
}