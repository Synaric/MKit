package com.synaric.mkit.vm

import android.net.Uri
import android.text.TextUtils
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.synaric.mkit.base.api.PagingInteractor
import com.synaric.mkit.base.api.Parameters
import com.synaric.mkit.base.const.AppConfig
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.data.repo.InitializeRepository
import com.synaric.mkit.data.repo.TradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val initializeRepository = InitializeRepository()

    private val tradeRecordList = object : PagingInteractor<MyParameters, TradeRecordAndGoods>() {

        override fun createObservable(params: MyParameters): Flow<PagingData<TradeRecordAndGoods>> {
            return Pager(
                params.pagingConfig
            ) {
                if (TextUtils.isEmpty(params.keyword)) {
                    TradeRepository().queryTradeRecordList()
                } else {
                    TradeRepository().queryTradeRecordListByKeyword(params.keyword ?: "")
                }
            }.flow
        }
    }

    val tradeRecordListPager = tradeRecordList.flow.cachedIn(viewModelScope)

    data class MyParameters(
        val keyword: String? = null,
        override val pagingConfig: PagingConfig
    ) : Parameters<TradeRecordAndGoods>

    val searchKeyword = mutableStateOf("")

    fun initInsert() {
        viewModelScope.launch {
            initializeRepository.initInsert()

            tradeRecordList(
                MyParameters("", PagingConfig(
                    pageSize = AppConfig.PagingSize,
                    enablePlaceholders = true,
                    maxSize = AppConfig.PagingMax
                ))
            )
        }
    }

    fun search(input: String?) {
        tradeRecordList(
            MyParameters(input, PagingConfig(
                pageSize = AppConfig.PagingSize,
                enablePlaceholders = true,
                maxSize = AppConfig.PagingMax
            ))
        )
    }

    fun exportDB(onCreateFile: (uri: Uri, filename: String) -> Unit) {
        viewModelScope.launch {
            initializeRepository.exportDB(onCreateFile)
        }
    }
}