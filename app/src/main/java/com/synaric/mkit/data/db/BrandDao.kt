package com.synaric.mkit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.synaric.mkit.data.entity.Brand

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(brand: Brand)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(brandList: List<Brand>)
}