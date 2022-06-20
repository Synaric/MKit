package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class GoodsExtendInfo(

    /**
     * 线材长度，单位：米。
     */
    @ColumnInfo(name = "length") var length: Float?,

    /**
     * 线材类型
     *
     * -1：（默认）未知
     *
     * 0：数字线
     *
     * 1：信号线
     *
     * 2：喇叭线
     *
     * 3：电源线
     *
     * 4：耳机线
     */
    @ColumnInfo(name = "cableType", defaultValue = "-1") var cableType: Int?,
)

enum class CableType(val type: Int) {

    UNKNOWN(-1),
    DIGITAL(0),
    INTERCONNECT(1),
    SPEAKER(2),
    POWER(3),
    HEADPHONE(4)
}
