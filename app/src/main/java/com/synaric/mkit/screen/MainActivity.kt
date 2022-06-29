package com.synaric.mkit.screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.synaric.art.BaseActivity
import com.synaric.mkit.base.theme.MKitTheme
import com.synaric.mkit.base.theme.MyColor
import com.synaric.mkit.base.theme.MySize
import com.synaric.mkit.composable.LazyLoadColumn
import com.synaric.mkit.composable.TradeRecord
import com.synaric.mkit.data.entity.BottomTab
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun CreateView() {
        // HorizontalPager与BottomNavigation状态
        val currentSelected = remember { mutableStateOf(0) }
        val bottomTabs = listOf(
            BottomTab("home", Icons.Filled.Home),
            BottomTab("my", Icons.Filled.Person)
        )
        val pagerState = rememberPagerState()

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
                            .padding(
                                0.dp,
                                0.dp,
                                0.dp,
                                MySize.BottomNavigationHeight
                            )   // 留出空间适配底部导航
                    ) {
                        HorizontalPager(
                            count = bottomTabs.size,
                            modifier = Modifier.fillMaxSize(),
                            state = pagerState,
                            userScrollEnabled = true
                        ) { page ->
                            SideEffect {
                                AppLog.d(this@MainActivity, "page: $page")
                                currentSelected.value = page
                            }
                            if (page == 0) {
                                MainScreen()
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
        currentSelected: MutableState<Int>,
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
                    selected = index == currentSelected.value,
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
    fun MainScreen() {
        val tradeRecordList = model.tradeRecordListPager.collectAsLazyPagingItems()
        val searchKeyword = model.searchKeyword.value
        val onSearchBarValueChange = { keyword: String ->
            model.searchKeyword.value = keyword
        }
        val onSearch = {
            model.search(searchKeyword)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 20.dp, 0.dp, 0.dp)
        ) {
            SearchBar(
                searchKeyword,
                onSearchBarValueChange,
                onSearch
            )
            TradeRecordList(tradeRecordList)
        }
    }

    @Composable
    fun MyScreen() {
        Text(text = "My")
    }

    @Composable
    fun SearchBar(
        searchKeyword: String,
        onValueChange: (String) -> Unit,
        onSearch: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MySize.SearchBarHeight)
                .padding(MySize.ScreenHorizontalPadding, 0.dp)
                .background(
                    color = MyColor.Text333,
                    shape = RoundedCornerShape(15.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = searchKeyword,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .height(22.dp)
                    .padding(20.dp, 0.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                ),
                maxLines = 1,
                cursorBrush = Brush.verticalGradient(listOf(Color.White, Color.White))
            )
            Button(
                onClick = onSearch) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
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