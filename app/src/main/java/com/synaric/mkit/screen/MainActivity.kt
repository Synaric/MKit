package com.synaric.mkit.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.synaric.art.BaseActivity
import com.synaric.mkit.composable.LazyLoadColumn
import com.synaric.mkit.composable.TradeRecord
import com.synaric.mkit.data.entity.BottomTab
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.theme.BOTTOM_NAVIGATION_HEIGHT
import com.synaric.mkit.theme.MKitTheme
import com.synaric.mkit.util.AppLog
import com.synaric.mkit.vm.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        // HorizontalPager与BottomNavigation状态
        val currentSelected = remember { mutableStateOf("home") }
        val bottomTabs = listOf(
            BottomTab("home", Icons.Filled.Home),
            BottomTab("my", Icons.Filled.Person)
        )
        val pagerState = rememberPagerState(bottomTabs.size)

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
                        HomeBottomNavigation(pagerState, currentSelected, bottomTabs)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 0.dp, 0.dp, BOTTOM_NAVIGATION_HEIGHT)   // 留出空间适配底部导航
                    ) {
                        HorizontalPager(
                            count = bottomTabs.size,
                            modifier = Modifier.fillMaxSize(),
                            state = pagerState,
                            userScrollEnabled = false
                        ) { page ->
                            SideEffect {
                                AppLog.d(this@MainActivity, "page: $page")
                            }
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
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun HomeBottomNavigation(
        pagerState: PagerState,
        currentSelected: MutableState<String>,
        bottomTabs: List<BottomTab>
    ) {
        val scope = rememberCoroutineScope()

        BottomNavigation {
            bottomTabs.forEachIndexed { index, screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            screen.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(screen.name) },
                    selected = screen.name == currentSelected.value,
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
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
            AppLog.d("model.composeCount.value", model.composeCount.value.toString())
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