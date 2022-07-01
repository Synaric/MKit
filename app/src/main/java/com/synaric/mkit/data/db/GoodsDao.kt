package com.synaric.mkit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.synaric.mkit.data.entity.Goods

@Dao
interface GoodsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(goods: Goods)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(goodsList: List<Goods>)

    @Query("SELECT * FROM Goods LIMIT (:start * :limit), :limit")
    fun getGoodsList(start: Int, limit: Int): List<Goods>
}