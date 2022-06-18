package com.synaric.mkit.data.repo

import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.Goods
import com.synaric.mkit.data.entity.GoodsExtendInfo
import com.synaric.mkit.data.entity.TradeRecord
import com.synaric.mkit.data.entity.TradeRecordAndGoods
import java.util.*

class TradeRepository : BaseRepository() {

    suspend fun testInsertAndQuery(): List<TradeRecordAndGoods> = execute {
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

        appDatabase.tradeRecordDao().getTradeRecordAndGoods()
    }
}