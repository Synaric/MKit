package com.synaric.mkit.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TradeRecord(

    @PrimaryKey(autoGenerate = true) val id: Int?,

    @ColumnInfo(name = "goodsId") val goodsId: Int?,

    @ColumnInfo(name = "actualPrice") var actualPrice: Int,

    @ColumnInfo(name = "listPrice") var listPrice: Int?,

    @ColumnInfo(name = "condition", defaultValue = "-1") var condition: Int,

    @ColumnInfo(name = "state", defaultValue = "-1") var state: Int,

    @ColumnInfo(name = "agent", defaultValue = "-1") var agent: Int,

    @ColumnInfo(name = "remark") var remark: String?,

    @Embedded var goodsExtendInfo: GoodsExtendInfo,

    @ColumnInfo(name = "tradeTime") var tradeTime: Date?,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

    )
