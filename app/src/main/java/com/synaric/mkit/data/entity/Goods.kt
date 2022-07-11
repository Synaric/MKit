package com.synaric.mkit.data.entity

import androidx.room.*
import java.util.*

@Entity
data class Goods(

    @PrimaryKey(autoGenerate = true) val goodsId: Int?,

    /**
     * 商品型号正式名。
     */
    @ColumnInfo(name = "model") var model: String,

    /**
     * 商品型号本地化正式名。
     */
    @ColumnInfo(name = "modelLocale") var modelLocale: String,

    /**
     * 商品型号别名。
     */
    @ColumnInfo(name = "modelAlias") var modelAlias: String,

    /**
     * 商品唯一标识。
     */
    @ColumnInfo(name = "brandId") val brandId: Int?,

    @ColumnInfo(name = "isDelete", defaultValue = "false") var isDelete: Boolean,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

    @ColumnInfo(name = "deleteTime") var deleteTime: Date?,

    )
