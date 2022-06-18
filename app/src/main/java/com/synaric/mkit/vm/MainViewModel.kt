package com.synaric.mkit.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synaric.mkit.data.entity.TradeRecordAndGoods
import com.synaric.mkit.data.repo.TradeRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val tradeList = mutableStateListOf<TradeRecordAndGoods>()

    fun testInsertAndQuery() {
        viewModelScope.launch {
            val list = TradeRepository().testInsertAndQuery()
            tradeList.addAll(list)
        }
    }
}