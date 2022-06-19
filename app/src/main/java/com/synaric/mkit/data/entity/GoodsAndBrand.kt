package com.synaric.mkit.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GoodsAndBrand(

    @Embedded
    val detail: Goods,

    @Relation(
        parentColumn = "brandId",
        entityColumn = "brandId"
    )
    val brand: Brand,
)