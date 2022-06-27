package com.synaric.mkit.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.synaric.mkit.base.theme.MySize

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T: Any> LazyLoadColumn(
    list: LazyPagingItems<T>,
    key: (item: T) -> Int,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(MySize.ScreenHorizontalPadding, 0.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
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
            key = key,
            itemContent = {item ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()) {
                    itemContent(item)
                }
            }
        )

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