package com.synaric.mkit.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synaric.art.BaseApplication
import com.synaric.mkit.db.AppDatabase
import com.synaric.mkit.entity.Goods
import com.synaric.mkit.entity.GoodsExtendInfo
import com.synaric.mkit.entity.TradeRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel: ViewModel() {

    fun testInsert() {
        Log.d("tradeRecordAndGoods", "insert")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("tradeRecordAndGoods", "start")
            val appDatabase = AppDatabase.getInstance(BaseApplication.instance)
            appDatabase.goodsDao().insert(
                Goods(
                    null,
                    "model a",
                    "br",
                    Date(),
                    Date()
                )
            )
            appDatabase.goodsDao().insert(
                Goods(
                    null,
                    "model b",
                    "br",
                    Date(),
                    Date()
                )
            )
            appDatabase.tradeRecordDao().insert(
                TradeRecord(
                    null,
                    1,
                    10,
                    10,
                    0,
                    1,
                    2,
                    "1",
                    GoodsExtendInfo(
                        2f,
                        1
                    ),
                    Date(),
                    Date(),
                    Date()
                )
            )
            appDatabase.tradeRecordDao().insert(
                TradeRecord(
                    null,
                    1,
                    20,
                    20,
                    0,
                    1,
                    2,
                    "2",
                    GoodsExtendInfo(
                        2f,
                        1
                    ),
                    Date(),
                    Date(),
                    Date()
                )
            )

            val tradeRecordAndGoods = appDatabase.tradeRecordDao().getTradeRecordAndGoods()
            Log.d("tradeRecordAndGoods", "tradeRecordAndGoods: $tradeRecordAndGoods")
        }
    }
}