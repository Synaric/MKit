package com.synaric.mkit.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TradeRecordAndGoods(

    @Embedded
    val tradeRecord: TradeRecord,

    @Relation(
        parentColumn = "goodsId",
        entityColumn = "id"
    )

    val goods: Goods
) {

    companion object {

        fun createEmptyObject() {
            fun create(): TradeRecordAndGoods {
                return TradeRecordAndGoods(
                    TradeRecord(
                        -1,
                        null,
                        0,
                        0,
                        -1,
                        -1,
                        -1,
                        "",
                        GoodsExtendInfo(
                            -1f,
                            -1
                        ),
                        null,
                        null,
                        null
                    ),
                    Goods(
                        -1,
                        "",
                        "",
                        null,
                        null
                    )
                )
            }
        }
    }
}