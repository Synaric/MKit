package com.synaric.mkit.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.synaric.mkit.data.entity.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex

@Dao
interface TradeRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradeRecord: TradeRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tradeRecordList: List<TradeRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSearchIndex(tradeRecordList: List<TradeRecordSearchIndex>)

    @Query("SELECT * FROM TradeRecordSearchIndex")
    fun queryAllSearchIndex(): List<TradeRecordSearchIndex>

    @Query("SELECT * FROM TradeRecordSearchIndex WHERE indexText MATCH :key")
    fun querySearchIndexByKey(key: String): List<TradeRecordSearchIndex>

    @Transaction
    @Query("SELECT * FROM TradeRecord ORDER BY goodsId DESC")
    fun getTradeRecordAndGoods(): PagingSource<Int, TradeRecordAndGoods>
}