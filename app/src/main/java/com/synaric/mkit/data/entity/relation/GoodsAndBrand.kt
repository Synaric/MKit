package com.synaric.mkit.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.synaric.mkit.data.entity.Brand
import com.synaric.mkit.data.entity.Goods

data class GoodsAndBrand(

    @Embedded
    val detail: Goods,

    @Relation(
        parentColumn = "brandId",
        entityColumn = "brandId"
    )
    val brand: Brand,
)