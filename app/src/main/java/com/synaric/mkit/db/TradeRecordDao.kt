package com.synaric.mkit.db

import androidx.room.*
import com.synaric.mkit.entity.TradeRecord
import com.synaric.mkit.entity.TradeRecordAndGoods

@Dao
interface TradeRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tradeRecord: TradeRecord)

    @Transaction
    @Query("SELECT * FROM TradeRecord")
    fun getTradeRecordAndGoods(): List<TradeRecordAndGoods>
}