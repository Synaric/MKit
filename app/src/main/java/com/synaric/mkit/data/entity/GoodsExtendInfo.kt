package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class GoodsExtendInfo(

    @ColumnInfo(name = "length", defaultValue = "-1") val length: Float?,

    @ColumnInfo(name = "connector", defaultValue = "-1") val connector: Int?,
)
