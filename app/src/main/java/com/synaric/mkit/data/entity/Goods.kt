package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Goods(

    @PrimaryKey(autoGenerate = true) val id: Int?,

    @ColumnInfo(name = "model") var model: String,

    @ColumnInfo(name = "brand") var brand: String,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

)
