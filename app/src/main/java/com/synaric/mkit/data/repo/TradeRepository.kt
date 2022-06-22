package com.synaric.mkit.data.repo

import androidx.paging.PagingSource
import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.*
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.util.StringUtil
import java.util.*

class TradeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.INSTANCE)

    fun queryTradeRecordList(): PagingSource<Int, TradeRecordAndGoods> {
        return appDatabase.tradeRecordDao().getTradeRecordAndGoods()
    }

    suspend fun initInsert() = execute {
        val initDate = Date()
        initInsertBrand(initDate)
        initInsertGoods(initDate)
        initInsertTradeRecord(initDate)
    }

    private fun initInsertBrand(initDate: Date) {
        val brandList = listOf(
            Brand(
                0,
                "Nordost",
                "音乐丝带",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                1,
                "Esoteric",
                "第一极品",
                "二嫂",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                2,
                "Van den Hul",
                "范登豪",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                3,
                "Siltech",
                "银彩",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                4,
                "Live Cable",
                "现场拉阔",
                "LC",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                5,
                "Crystal Connect",
                "晶彩",
                "Crystal Cable",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                6,
                "Kubala Sosna",
                "金威",
                "KS",
                false,
                initDate,
                initDate,
                null,
            )
        )
        appDatabase.brandDao().insertAll(brandList)
    }

    private fun initInsertGoods(initDate: Date) {


        val goodsList = listOf(
            Goods(
                0,
                "Odin2",
                "奥丁2",
                "",
                0,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                1,
                "Grandioso K1X",
                "",
                "",
                1,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                2,
                "Mainsstream",
                "大主流",
                "VDH",
                2,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                3,
                "Triple Crown",
                "三皇冠",
                "",
                3,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                4,
                "Signature XLR",
                "签名XLR",
                "",
                4,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                5,
                "Valhalla2 RCA",
                "瓦哈拉2 RCA",
                "",
                0,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                6,
                "Da vinci",
                "达芬奇",
                "Davinci",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                7,
                "Monet",
                "莫奈",
                "",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                8,
                "Realization",
                "钻星",
                "",
                6,
                false,
                initDate,
                initDate,
                null,
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
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                1,
                0,
                45000,
                142000,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.25f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                2,
                1,
                158000,
                240000,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                3,
                2,
                2370,
                3340,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-6-19"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                4,
                3,
                41000,
                45000,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2021-9-6"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                5,
                4,
                21800,
                33000,
                Condition.NEW,
                Change.NEW,
                SalesChannel.PARALLEL,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.INTERCONNECT
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                6,
                5,
                40000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.INTERCONNECT
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                7,
                0,
                69000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    2.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                8,
                6,
                58000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                9,
                7,
                18000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                10,
                8,
                18500,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.LICENCED,
                "",
                GoodsExtendInfo(
                    1.8f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-1"),
                false,
                initDate,
                initDate,
                null,
            ),
        )
        appDatabase.tradeRecordDao().insertAll(tradeRecordList)
    }
}