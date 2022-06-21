@file:OptIn(ExperimentalMaterial3Api::class)

package com.synaric.mkit.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.synaric.mkit.util.StringUtil

@Composable
fun TradeRecord(record: TradeRecordAndGoods) {
    val title = StringUtil.formatTradeRecordTitle(record)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 20.dp)) {
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
                    text = "￥${record.tradeRecord.actualPrice}",
                    color = ActualPrice,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
                if (record.tradeRecord.listPrice != null) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "￥${record.tradeRecord.listPrice}",
                        color = Text999,
                        fontSize = 10.sp,
                        textDecoration = TextDecoration.LineThrough,
                        lineHeight = 10.sp
                    )
                }
            }
        }

    }
}