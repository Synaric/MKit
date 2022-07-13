package com.synaric.mkit.data.repo

import com.synaric.art.BaseApplication
import com.synaric.art.BaseRepository
import com.synaric.mkit.data.db.AppDatabase
import com.synaric.mkit.data.entity.Brand

class BrandRepository : BaseRepository() {

    private val appDatabase = AppDatabase.getInstance(BaseApplication.Instance)

    suspend fun getSimilarBrandList(brand: String): List<Brand> = execute {
        appDatabase.brandDao().getSimilarBrandList(brand)
    }

    suspend fun insert(brand: Brand) = execute {
        appDatabase.brandDao().insert(brand)
    }
}