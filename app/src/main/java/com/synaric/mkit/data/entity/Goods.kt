package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Goods(

    @PrimaryKey(autoGenerate = true) val id: Int?,

    /**
     * 商品型号正式名
     */
    @ColumnInfo(name = "model") var model: String,

    /**
     * 商品型号本地化正式名
     */
    @ColumnInfo(name = "modelLocale") var modelLocale: String,

    /**
     * 商品型号别名
     */
    @ColumnInfo(name = "modelAlias") var modelAlias: String,

    /**
     * 商品品牌正式名
     */
    @ColumnInfo(name = "brand") var brand: String,

    /**
     * 商品品牌本地化正式名
     */
    @ColumnInfo(name = "brandLocale") var brandLocale: String,

    /**
     * 商品品牌别名
     */
    @ColumnInfo(name = "brandAlias") var brandAlias: String,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

)
