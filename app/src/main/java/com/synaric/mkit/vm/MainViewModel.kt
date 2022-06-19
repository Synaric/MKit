package com.synaric.mkit.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.synaric.mkit.data.repo.TradeRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val tradeRecordListPager = Pager(
        PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            maxSize = PagingConfig.MAX_SIZE_UNBOUNDED
        )
    ) {
        TradeRepository().queryTradeRecordList()
    }

    fun testInsert() {
        viewModelScope.launch {
            TradeRepository().initInsert()
        }
    }
}