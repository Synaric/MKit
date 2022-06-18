package com.synaric.mkit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.synaric.art.BaseActivity
import com.synaric.mkit.data.entity.TradeRecordAndGoods
import com.synaric.mkit.theme.MKitTheme
import com.synaric.mkit.vm.MainViewModel

class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateView()
        }
        model.testInsertAndQuery()
    }

    @Composable
    fun CreateView() {
        val tradeList = model.tradeList
        MKitTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                TradeRecordList(tradeList)
            }
        }
    }

    @Composable
    fun TradeRecordList(list: List<TradeRecordAndGoods>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = list,
                key = { item ->
                    item.tradeRecord.id!!
                }
            ) { item ->
                Text("item.goods.model")
            }

        }
    }

    @Composable
    fun TestList(list: List<Int>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = list,
                key = { item -> item
                }
            ) { item ->
                Text("item.goods.model:$item")
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        CreateView()
    }
}