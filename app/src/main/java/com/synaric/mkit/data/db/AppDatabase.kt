package com.synaric.mkit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.synaric.mkit.data.entity.Brand
import com.synaric.mkit.data.entity.Goods
import com.synaric.mkit.data.entity.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordSearchIndex
import com.synaric.mkit.util.AppLog
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executors

@Database(entities = [
    Goods::class,
    TradeRecord::class,
    Brand::class,
    TradeRecordSearchIndex::class], version = 10)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun goodsDao(): GoodsDao
    abstract fun tradeRecordDao(): TradeRecordDao
    abstract fun brandDao(): BrandDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "SyMyKit.db")
                .setQueryCallback(
                    { sqlQuery, bindArgs ->
                        // 记录所有触发的查询
                        AppLog.d(this, "SQL Query $sqlQuery, bindArgs $bindArgs")
                    },
                    Executors.newFixedThreadPool(5)
                )
                .build()
    }
}
