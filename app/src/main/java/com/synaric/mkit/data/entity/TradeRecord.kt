package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TradeRecord(

    /**
     * 交易记录唯一标识
     */
    @PrimaryKey(autoGenerate = true) val tradeRecordId: Int?,

    /**
     * 商品唯一标识
     */
    @ColumnInfo(name = "goodsId") val goodsId: Int?,

    /**
     * 商品实际成交价
     */
    @ColumnInfo(name = "actualPrice") var actualPrice: Int,

    /**
     * 商品标价
     */
    @ColumnInfo(name = "listPrice") var listPrice: Int?,

    /**
     * 商品成色，范围0-100，例如100表示全新，85表示85新。
     *
     * -1：（默认）未知
     */
    @ColumnInfo(name = "condition", defaultValue = "-1") var condition: Int,

    /**
     * 商品换手数
     *
     * 0：（默认）未知
     *
     * 1：全新
     *
     * 2：二手（一手玩家出货）
     *
     * 3：疑似N手
     *
     * 4：N手
     */
    @ColumnInfo(name = "change", defaultValue = "0") var change: Int,

    /**
     * 商品渠道
     *
     * 0：（默认）未知
     *
     * 1：国行
     *
     * 2：水货
     */
    @ColumnInfo(name = "salesChannel", defaultValue = "0") var salesChannel: Int,

    /**
     * 交易情况备注，最多200字。
     */
    @ColumnInfo(name = "remark") var remark: String?,

    /**
     * 商品扩展信息，例如线材长度。
     */
    @Embedded var goodsExtendInfo: GoodsExtendInfo?,

    /**
     * 交易时间。例如当你知道本次交易在22.6.1发生，或者所填交易价格在22.6.1成立，那么tradeTime就是22.6.1。
     */
    @ColumnInfo(name = "tradeTime") var tradeTime: Date?,

    @ColumnInfo(name = "isDelete", defaultValue = "false") var isDelete: Boolean,

    @ColumnInfo(name = "createTime") var createTime: Date?,

    @ColumnInfo(name = "updateTime") var updateTime: Date?,

    @ColumnInfo(name = "deleteTime") var deleteTime: Date?,

    )

enum class Condition(val type: Int) {

    UNKNOWN(-1),
    NEW(100),
    CONDITION_95(95),
    CONDITION_90(90),
    CONDITION_80(80),
    CONDITION_70(70),
}

enum class Change(val type: Int) {

    UNKNOWN(-1),
    NEW(0),
    SECOND_HAND(1),
    LIKE_MULTI_HAND(2),
    MULTI_HAND(3),
}

enum class SalesChannel(val type: Int) {

    CHANNEL_UNKNOWN(-1),
    CHANNEL_LICENCED(0),
    CHANNEL_PARALLEL(1),
}
