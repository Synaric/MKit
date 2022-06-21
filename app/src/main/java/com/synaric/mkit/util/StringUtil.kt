package com.synaric.mkit.util

import android.annotation.SuppressLint
import android.text.TextUtils
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
            var brand = record.goods.brand.brandLocale
            if (TextUtils.isEmpty(brand)) {
                brand = record.goods.brand.brand
            }
            var model = record.goods.detail.modelLocale
            if (TextUtils.isEmpty(model)) {
                model = record.goods.detail.model
            }
            var extend = ""
            val goodsExtendInfo = record.tradeRecord.goodsExtendInfo
            goodsExtendInfo?.let {
                extend += if (it.length == null) "" else "${it.length}M"
                if (CableType.UNKNOWN != it.cableType) {
                    extend += it.cableType?.alias ?: ""
                }
            }
            return "$brand$model $extend"
        }
    }
}