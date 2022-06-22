package com.synaric.mkit.util

import android.annotation.SuppressLint
import android.text.TextUtils
import com.synaric.art.BaseApplication
import com.synaric.mkit.R
import com.synaric.mkit.data.entity.CableType
import com.synaric.mkit.data.entity.Change
import com.synaric.mkit.data.entity.Condition
import com.synaric.mkit.data.entity.SalesChannel
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
            var brand = record.goods.brand.brand
            brand += record.goods.brand.brandLocale
            var model = record.goods.detail.model
            model += record.goods.detail.modelLocale
            var extend = ""
            val goodsExtendInfo = record.tradeRecord.goodsExtendInfo
            goodsExtendInfo?.let {
                extend +=
                    if (it.length == null) ""
                    else "${it.length}${BaseApplication.INSTANCE.getString(R.string.meter)}"
                if (CableType.UNKNOWN != it.cableType) {
                    extend += it.cableType?.alias ?: ""
                }
            }
            return "$brand$model $extend"
        }

        fun collectTags(record: TradeRecordAndGoods): List<String> {
            val change = record.tradeRecord.change
            val salesChannel = record.tradeRecord.salesChannel
            val condition = record.tradeRecord.condition
            val tagList = mutableListOf<String>()
            if (change != Change.UNKNOWN) {
                tagList.add(change.alias)
            }
            if (salesChannel != SalesChannel.UNKNOWN) {
                tagList.add(salesChannel.alias)
            }
            if (condition != Condition.UNKNOWN) {
                tagList.add("${condition.type}%new")
            }

            return tagList
        }
    }
}