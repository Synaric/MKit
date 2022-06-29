package com.synaric.mkit.activity

import android.annotation.SuppressLint
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.synaric.art.BaseActivity
import com.synaric.mkit.base.theme.MKitTheme
import com.synaric.mkit.base.theme.MySize
import com.synaric.mkit.data.entity.BottomTab
import com.synaric.mkit.screen.MainScreen
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
                                MainScreen(model)
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
    fun MyScreen() {
        Text(text = "My")
    }


//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        TradeRecord(TradeRecordAndGoods.createPreviewObject())
//    }
}