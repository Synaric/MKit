package com.synaric.mkit.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.synaric.mkit.data.entity.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods

@Dao
interface TradeRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradeRecord: TradeRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tradeRecordList: List<TradeRecord>)

    @Transaction
    @Query("SELECT * FROM TradeRecord")
    fun getTradeRecordAndGoods(): PagingSource<Int, TradeRecordAndGoods>
}