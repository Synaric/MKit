package com.synaric.mkit.data.repo

import androidx.paging.PagingSource
import androidx.room.Fts4
import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex

class TradeRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.Instance)

    /**
     * 查询所有交易记录
     * @return PagingSource<Int, TradeRecordAndGoods> 交易记录列表
     */
    fun queryTradeRecordList(): PagingSource<Int, TradeRecordAndGoods> {
        return appDatabase.tradeRecordDao().getTradeRecordAndGoodsList()
    }

    /**
     * 关键字查询交易记录。查询使用[Fts4] icu模式。
     * @param keyword String 关键字
     * @return List<TradeRecordAndGoods> 交易记录快照列表，请使用快照内的[TradeRecordSearchIndex.tradeRecordId]查询实际数据。
     */
    fun queryTradeRecordListByKeyword(keyword: String): PagingSource<Int, TradeRecordAndGoods> {
        return appDatabase.tradeRecordDao().getSearchIndexByKey("$keyword*")
    }


}