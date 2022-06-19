package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Brand(

    @PrimaryKey(autoGenerate = true) val brandId: Int?,

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

    @ColumnInfo(name = "isDelete", defaultValue = "false") var isDelete: Boolean,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

    @ColumnInfo(name = "deleteTime") var deleteTime: Date?,
)