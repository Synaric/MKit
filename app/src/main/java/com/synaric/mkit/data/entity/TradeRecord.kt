package com.synaric.mkit.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.synaric.art.BaseApplication
import com.synaric.mkit.R
import java.util.*

@Entity
data class TradeRecord(

    /**
     * 交易记录唯一标识。
     */
    @PrimaryKey(autoGenerate = true) val tradeRecordId: Int?,

    /**
     * 商品唯一标识。
     */
    @ColumnInfo(name = "goodsId") val goodsId: Int?,

    /**
     * 商品实际成交价。
     */
    @ColumnInfo(name = "actualPrice") var actualPrice: Int,

    /**
     * 商品标价。
     */
    @ColumnInfo(name = "listPrice") var listPrice: Int?,

    /**
     * 商品成色，范围0-100，例如100表示全新，85表示85新。
     *
     * -1：（默认）未知
     */
    @ColumnInfo(name = "condition", defaultValue = "-1") var condition: Condition,

    /**
     * 商品换手数。
     *
     * 0：（默认）未知
     *
     * 1：全新
     *
     * 2：疑似二手
     *
     * 3：二手
     *
     * 4：疑似N手
     *
     * 5：N手
     */
    @ColumnInfo(name = "change", defaultValue = "0") var change: Change,

    /**
     * 商品销售渠道。
     *
     * 0：（默认）未知
     *
     * 1：国行
     *
     * 2：水货
     */
    @ColumnInfo(name = "salesChannel", defaultValue = "0") var salesChannel: SalesChannel,

    /**
     * 商品购买店铺。
     *
     * 对于二手商品，填写商品全新时交易的店铺名。
     */
    @ColumnInfo(name = "shop", defaultValue = "0") var shop: String?,

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

/**
 * 商品成色的枚举。
 * @property type Int
 * @constructor
 */
enum class Condition(val type: Int) {

    UNKNOWN(-1),
    NEW(100),
    CONDITION_95(95),
    CONDITION_90(90),
    CONDITION_85(85),
    CONDITION_80(80),
    CONDITION_70(70),
}

/**
 * 商品换手数的枚举。
 * @property type Int
 * @property alias String
 * @constructor
 */
enum class Change(val type: Int, val alias: String) {

    UNKNOWN(0, BaseApplication.Instance.resources.getStringArray(R.array.change)[0]),
    NEW(1, BaseApplication.Instance.resources.getStringArray(R.array.change)[1]),
    LIKE_SECOND_HAND(2, BaseApplication.Instance.resources.getStringArray(R.array.change)[2]),
    SECOND_HAND(3, BaseApplication.Instance.resources.getStringArray(R.array.change)[3]),
    LIKE_MULTI_HAND(4, BaseApplication.Instance.resources.getStringArray(R.array.change)[4]),
    MULTI_HAND(5, BaseApplication.Instance.resources.getStringArray(R.array.change)[5]),
}

/**
 * 商品销售渠道的枚举。
 * @property type Int
 * @property alias String
 * @constructor
 */
enum class SalesChannel(val type: Int, val alias: String) {

    UNKNOWN(0, BaseApplication.Instance.resources.getStringArray(R.array.salesChannel)[0]),
    LICENCED(1, BaseApplication.Instance.resources.getStringArray(R.array.salesChannel)[1]),
    PARALLEL(2, BaseApplication.Instance.resources.getStringArray(R.array.salesChannel)[2]),
}
