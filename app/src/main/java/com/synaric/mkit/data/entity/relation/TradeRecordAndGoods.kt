package com.synaric.mkit.data.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.synaric.mkit.data.entity.*
import java.util.*

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

        fun createPreviewObject(): TradeRecordAndGoods {
            val sample = createEmptyObject()
            sample.tradeRecord.apply {
                actualPrice = 1000
                listPrice = 1500
                condition = Condition.UNKNOWN.type
                change = Change.UNKNOWN.type
                goodsExtendInfo?.apply {
                    cableType = CableType.DIGITAL.type
                    length = 1.5f
                }
                tradeTime = Date()
            }
            sample.goods.apply {
                detail.apply {
                    model = "model"
                    modelLocale = "modelLocale"
                }
                brand.apply {
                    brand = "brand"
                    brandLocale = "brandLocale"
                }
            }

            return sample
        }
    }
}