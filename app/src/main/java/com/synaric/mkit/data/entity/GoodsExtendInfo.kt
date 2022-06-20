package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.synaric.art.BaseApplication
import com.synaric.mkit.R

@Entity
data class GoodsExtendInfo(

    /**
     * 线材长度，单位：米。
     */
    @ColumnInfo(name = "length") var length: Float?,

    /**
     * 线材类型
     *
     * 0：（默认）未知
     *
     * 1：数字线
     *
     * 2：信号线
     *
     * 3：喇叭线
     *
     * 4：电源线
     *
     * 5：耳机线
     */
    @ColumnInfo(name = "cableType", defaultValue = "0") var cableType: Int?,
)

enum class CableType(val type: Int, val alias: String) {

    UNKNOWN(0, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[0]),
    DIGITAL(1, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[1]),
    INTERCONNECT(2, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[2]),
    SPEAKER(3, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[3]),
    POWER(4, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[4]),
    HEADPHONE(5, BaseApplication.instance.resources.getStringArray(R.array.cable_type)[5])
}
