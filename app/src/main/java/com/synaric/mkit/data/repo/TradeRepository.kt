package com.synaric.mkit.data.repo

import androidx.paging.PagingSource
import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.*
import com.synaric.mkit.util.StringUtil
import java.util.*

class TradeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.instance)

    fun queryTradeRecordList(): PagingSource<Int, TradeRecordAndGoods> {
        return appDatabase.tradeRecordDao().getTradeRecordAndGoods()
    }

    suspend fun initInsert() = execute {
        val initDate = Date()
        initInsertGoods(initDate)
        initInsertTradeRecord(initDate)
    }
    
    private fun initInsertGoods(initDate: Date) {
        val goodsList = listOf(
            Goods(
                0,
                "Odin2",
                "奥丁2",
                "",
                "Nordost",
                "音乐丝带",
                "",
                initDate,
                initDate
            ),
            Goods(
                1,
                "Grandioso K1X",
                "",
                "",
                "Esoteric",
                "第一极品",
                "二嫂",
                initDate,
                initDate
            ),
            Goods(
                2,
                "Mainsstream",
                "大主流",
                "VDH",
                "Van den Hul",
                "范登豪",
                "",
                initDate,
                initDate
            ),
        )
        appDatabase.goodsDao().insertAll(goodsList)
    }
    
    private fun initInsertTradeRecord(initDate: Date) {
        val tradeRecordList = listOf(
            TradeRecord(
                0,
                0,
                58000,
                142000,
                Condition.NEW.type,
                Change.NEW.type,
                SalesChannel.CHANNEL_LICENCED.type,
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER.type
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                initDate,
                initDate
            ),
            TradeRecord(
                1,
                0,
                45000,
                142000,
                Condition.CONDITION_90.type,
                Change.SECOND_HAND.type,
                SalesChannel.CHANNEL_LICENCED.type,
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER.type
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                initDate,
                initDate
            ),
            TradeRecord(
                2,
                1,
                158000,
                240000,
                Condition.NEW.type,
                Change.NEW.type,
                SalesChannel.CHANNEL_LICENCED.type,
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                initDate,
                initDate
            ),
            TradeRecord(
                3,
                2,
                2370,
                3340,
                Condition.NEW.type,
                Change.NEW.type,
                SalesChannel.CHANNEL_LICENCED.type,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER.type
                ),
                StringUtil.dateStrToDate("2022-6-19"),
                initDate,
                initDate
            )
        )
        appDatabase.tradeRecordDao().insertAll(tradeRecordList)
    }
}