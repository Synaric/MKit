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

        private fun createEmptyObject(): TradeRecordAndGoods {
            return TradeRecordAndGoods(
                TradeRecord(
                    -1,
                    null,
                    0,
                    0,
                    Condition.UNKNOWN,
                    Change.UNKNOWN,
                    SalesChannel.UNKNOWN,
                    "",
                    "",
                    GoodsExtendInfo(
                        -1f,
                        CableType.UNKNOWN
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
                        GoodsType.OTHER,
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
                condition = Condition.UNKNOWN
                change = Change.UNKNOWN
                goodsExtendInfo?.apply {
                    cableType = CableType.DIGITAL
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