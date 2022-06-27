package com.synaric.mkit.vm

import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.synaric.mkit.data.repo.TradeRepository
import com.synaric.mkit.util.AppLog
import kotlinx.coroutines.Dispatchers
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

    val composeCount = mutableStateOf(0)

    val searchKeyword = mutableStateOf("")

    fun initInsert() {
        viewModelScope.launch(Dispatchers.IO) {
            TradeRepository().initInsert()
        }
    }

    fun search(keyword: String?) {
//        if (TextUtils.isEmpty(keyword)) {
//            return
//        }

        viewModelScope.launch(Dispatchers.IO) {
            val list =
                TradeRepository().queryTradeRecordListByKeyword("丝带")
            AppLog.d(this@MainViewModel, "-------------result--------------")
            list.forEach {
                AppLog.d(this@MainViewModel, it.toString())
            }
        }
    }
}