package com.synaric.mkit.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.theme.ActualPrice
import com.synaric.mkit.theme.Text999

@Composable
fun TradeRecord(record: TradeRecordAndGoods) {

    val brand = record.goods.brand.brandLocale
    val model = record.goods.detail.modelLocale
    var extend = ""
    val goodsExtendInfo = record.tradeRecord.goodsExtendInfo
    goodsExtendInfo?.let {
        extend += if (it.length == null) "" else "$it.lengthM"
        extend += if (it.cableType != -1) it.cableType else ""
    }
    val title = "$brand$model$extend"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp,
            lineHeight = 20.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "￥${record.tradeRecord.actualPrice})",
                color = ActualPrice,
                fontSize = 16.sp,
                lineHeight = 20.sp
            )
            if (record.tradeRecord.listPrice != null) {
                Text(
                    text = "￥${record.tradeRecord.listPrice})",
                    color = Text999,
                    fontSize = 10.sp,
                    textDecoration = TextDecoration.LineThrough,
                    lineHeight = 10.sp
                )
            }
        }
    }
}