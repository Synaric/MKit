package com.synaric.mkit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.synaric.mkit.base.theme.MyColor
import com.synaric.mkit.base.theme.MySize
import com.synaric.mkit.data.entity.relation.TradeRecordAndGoods
import com.synaric.mkit.view.LazyLoadColumn
import com.synaric.mkit.view.TradeRecord
import com.synaric.mkit.vm.MainViewModel

@Composable
fun MainScreen(model: MainViewModel) {
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
private fun SearchBar(
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
private fun TradeRecordList(list: LazyPagingItems<TradeRecordAndGoods>) {
    LazyLoadColumn(
        list = list,
        key = { index -> list[index]?.tradeRecord?.tradeRecordId!! },
    ) { index ->
        TradeRecord(list[index]!!)
    }
}