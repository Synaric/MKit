package com.synaric.mkit.data.repo

import androidx.paging.PagingSource
import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.art.util.SPUtil
import com.synaric.mkit.const.SPKey
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.*
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex
import com.synaric.mkit.util.AppLog
import com.synaric.mkit.util.StringUtil
import java.util.*

class TradeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.INSTANCE)

    fun queryTradeRecordList(): PagingSource<Int, TradeRecordAndGoods> {
        return appDatabase.tradeRecordDao().getTradeRecordAndGoods()
    }

    fun queryTradeRecordListByKeyword(keyword: String): List<TradeRecordSearchIndex> {
        return appDatabase.tradeRecordDao().querySearchIndexByKey("$keyword*")
    }

    suspend fun initInsert() = execute {
        val isInitDatabase = SPUtil.INSTANCE.getSpValue(SPKey.INIT_DATABASE, false)

        if (!isInitDatabase) {
            val initDate = Date()
            initSearchIndex(
                initInsertBrand(initDate),
                initInsertGoods(initDate),
                initInsertTradeRecord(initDate)
            )
            val searchIndexList = appDatabase.tradeRecordDao().queryAllSearchIndex()
            AppLog.d(this, "complete insert")
            searchIndexList.forEach {
                AppLog.d(this, it.toString())
            }

            SPUtil.INSTANCE.putSpValue(SPKey.INIT_DATABASE, true)
        } else {
            AppLog.d(this, "skip insert")
        }
    }

    private fun initSearchIndex(
        brandList: List<Brand>,
        goodsList: List<Goods>,
        tradeRecordList: List<TradeRecord>
    ) {
        val searchIndexList = mutableListOf<TradeRecordSearchIndex>()
        tradeRecordList.forEach { record ->
            if (record.tradeRecordId == null) {
                return
            }
            val goods = goodsList.findLast { it.goodsId?.equals(record.goodsId) ?: false }
            var brand: Brand? = null
            if (goods != null) {
                brand = brandList.findLast { it.brandId?.equals(goods.brandId) ?: false }
            }
            val searchIndex =
                StringUtil.formatTradeRecordSearchIndex(record, goods, brand)
            val fullText =
                StringUtil.formatTradeRecordFullText(record, goods, brand)
            val tradeRecordSearchIndex = TradeRecordSearchIndex(record.tradeRecordId, searchIndex, fullText)
            searchIndexList.add(tradeRecordSearchIndex)
        }
        appDatabase.tradeRecordDao().insertAllSearchIndex(searchIndexList)
    }

    private fun initInsertBrand(initDate: Date): List<Brand> {
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
            ),
            Brand(
                7,
                "Argento",
                "雅图",
                "",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                8,
                "Accuphase",
                "金嗓子",
                "喉宝|嗓子",
                false,
                initDate,
                initDate,
                null,
            ),
            Brand(
                9,
                "Creaktiv",
                "创新",
                "",
                false,
                initDate,
                initDate,
                null,
            )
        )
        appDatabase.brandDao().insertAll(brandList)
        return brandList
    }

    private fun initInsertGoods(initDate: Date): List<Goods> {


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
            Goods(
                9,
                "Ultimate Dream",
                "终极梦幻",
                "",
                5,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                10,
                "Flow",
                "",
                "",
                7,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                11,
                "C-3900",
                "",
                "",
                8,
                false,
                initDate,
                initDate,
                null,
            ),
            Goods(
                12,
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
                13,
                "Big Reference",
                "大参考",
                "",
                9,
                false,
                initDate,
                initDate,
                null,
            ),
        )
        appDatabase.goodsDao().insertAll(goodsList)
        return goodsList
    }

    private fun initInsertTradeRecord(initDate: Date): List<TradeRecord> {
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
                "名线名声",
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
                "Hifiboy",
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
                "名线名声",
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
                "熊猫视听",
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-3-24"),
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
                "",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-1-12"),
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
                "20A",
                GoodsExtendInfo(
                    2f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-5-11"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                11,
                9,
                35000,
                null,
                Condition.CONDITION_90,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "",
                "换过头",
                GoodsExtendInfo(
                    1.5f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-3-1"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                12,
                10,
                12000,
                18500,
                Condition.CONDITION_95,
                Change.SECOND_HAND,
                SalesChannel.PARALLEL,
                "V2Hifi",
                "",
                GoodsExtendInfo(
                    2f,
                    CableType.POWER
                ),
                StringUtil.dateStrToDate("2022-6-15"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                13,
                11,
                125900,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.PARALLEL,
                "乐海音响",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-5-27"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                14,
                12,
                110000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "淘乐音响",
                "",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2022-6-24"),
                false,
                initDate,
                initDate,
                null,
            ),
            TradeRecord(
                15,
                13,
                35000,
                null,
                Condition.NEW,
                Change.NEW,
                SalesChannel.LICENCED,
                "",
                "4层",
                GoodsExtendInfo(
                    null,
                    null
                ),
                StringUtil.dateStrToDate("2020-6-24"),
                false,
                initDate,
                initDate,
                null,
            ),
        )
        appDatabase.tradeRecordDao().insertAll(tradeRecordList)
        return tradeRecordList
    }
}