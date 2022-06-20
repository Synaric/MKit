package com.synaric.mkit.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.synaric.art.BaseActivity
import com.synaric.mkit.composable.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.theme.MKitTheme
import com.synaric.mkit.vm.MainViewModel

class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateView()
        }
        model.initInsert()
    }

    @Composable
    fun CreateView() {
        val tradeRecordList = model.tradeRecordListPager.flow.collectAsLazyPagingItems()
        MKitTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                TradeRecordList(tradeRecordList)
            }
        }
    }

    @Composable
    fun TradeRecordList(list: LazyPagingItems<TradeRecordAndGoods>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (list.loadState.refresh == LoadState.Loading) {
                item {
                    Text(
                        text = "Waiting for items to load from the backend",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            items(
                items = list,
                key = { item ->
                    item.tradeRecord.tradeRecordId!!
                }
            ) { item ->
                Text(
                    text = "item.goods.model${item}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically),
                    fontSize = 12.sp
                )
            }

            if (list.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TradeRecord(TradeRecordAndGoods.createPreviewObject())
    }
}