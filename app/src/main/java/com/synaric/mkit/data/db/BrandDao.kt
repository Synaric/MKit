package com.synaric.mkit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.synaric.mkit.data.entity.Brand

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(brand: Brand)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(brandList: List<Brand>)

    @Query("SELECT * FROM Brand LIMIT (:start * :limit), :limit")
    fun getBrandList(start: Int, limit: Int): List<Brand>

    @Query("SELECT * FROM Brand WHERE brand MATCH :s LIMIT 0, 3")
    fun getSimilarBrandList(s: String): List<Brand>
}