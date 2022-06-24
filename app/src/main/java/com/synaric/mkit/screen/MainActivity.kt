package com.synaric.mkit.screen

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.synaric.art.BaseActivity
import com.synaric.mkit.composable.LazyLoadColumn
import com.synaric.mkit.composable.TradeRecord
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.theme.MKitTheme
import com.synaric.mkit.vm.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : BaseActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateView()
        }
        model.initInsert()
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun CreateView() {
        val tradeRecordList = model.tradeRecordListPager.flow.collectAsLazyPagingItems()
        val currentSelected = remember { mutableStateOf("home") }
        val pagerState = rememberPagerState()
        val items = listOf(
            "home", "my"
        )

        MKitTheme(
            darkTheme = true
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation {
                            items.forEachIndexed { index, screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(screen) },
                                    selected = screen == currentSelected.value,
                                    onClick = {
//                                        pagerState.scrollToPage(index)
                                    }
                                )
                            }
                        }
                    }
                ) {
                    HorizontalPager(
                        count = 2,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        if (page == 0) {
                            MainScreen(list = tradeRecordList)
                        } else {
                            MyScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen(list: LazyPagingItems<TradeRecordAndGoods>) {
        TradeRecordList(list)
    }

    @Composable
    fun MyScreen() {
        Text(text = "My")
    }

    @Composable
    fun TradeRecordList(list: LazyPagingItems<TradeRecordAndGoods>) {
        SideEffect {
            model.composeCount.value++
            Log.d("model.composeCount.value", model.composeCount.value.toString())
        }
        LazyLoadColumn(
            list = list,
            key = { item -> item.tradeRecord.tradeRecordId!! },
        ) { item ->
            if (item != null) {
                TradeRecord(item)
            }
        }
    }

//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        TradeRecord(TradeRecordAndGoods.createPreviewObject())
//    }
}