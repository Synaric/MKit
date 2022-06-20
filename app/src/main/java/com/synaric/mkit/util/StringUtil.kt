package com.synaric.mkit.util

import android.annotation.SuppressLint
import com.synaric.mkit.data.entity.CableType
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import java.text.SimpleDateFormat
import java.util.*

class StringUtil {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun dateStrToDate(dateStr: String): Date {
            val pattern = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            return simpleDateFormat.parse(dateStr) ?: Date(0L)
        }

        fun formatTradeRecordTitle(record: TradeRecordAndGoods): String {
            val brand = record.goods.brand.brandLocale
            val model = record.goods.detail.modelLocale
            var extend = ""
            val goodsExtendInfo = record.tradeRecord.goodsExtendInfo
            goodsExtendInfo?.let {
                extend += if (it.length == null) "" else "${it.length}M"
                extend += if (it.cableType != null) formatCableType(it.cableType) else ""
            }
            return "$brand$model$extend"
        }

        private fun formatCableType(type: Int?): String {
            val arrayOfCableTypes = CableType.values()
            for (ct: CableType in arrayOfCableTypes) {
                if (ct.type == type) {
                    return ct.name
                }
            }

            return ""
        }
    }
}