package com.synaric.mkit.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.synaric.mkit.data.entity.Brand
import com.synaric.mkit.data.entity.Goods
import com.synaric.mkit.data.entity.GoodsExtendInfo
import com.synaric.mkit.data.entity.TradeRecord

data class TradeRecordAndGoods(

    @Embedded
    val tradeRecord: TradeRecord,

    @Relation(
        entity = Goods::class,
        parentColumn = "goodsId",
        entityColumn = "goodsId"
    )
    val goods: GoodsAndBrand,
) {

    companion object {

        fun createEmptyObject(): TradeRecordAndGoods {
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
                    false,
                    null,
                    null,
                    null,
                ),
                GoodsAndBrand(
                    Goods(
                        -1,
                        "",
                        "",
                        "",
                        brandId = 1,
                        false,
                        null,
                        null,
                        null,
                    ),
                    Brand(
                        -1,
                        "",
                        "",
                        "",
                        false,
                        null,
                        null,
                        null,
                    )
                )
            )
        }
    }
}