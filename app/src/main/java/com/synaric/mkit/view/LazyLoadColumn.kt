package com.synaric.mkit.view

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
import com.synaric.mkit.base.theme.MySize

/**
 * An app style [LazyColumn] with default refreshing and loading view.
 * @param list LazyPagingItems<T>
 * @param key Function1<[@kotlin.ParameterName] T, Int>
 * @param itemContent [@androidx.compose.runtime.Composable] [@kotlin.ExtensionFunctionType] Function2<LazyItemScope, [@kotlin.ParameterName] T?, Unit>
 * @return Unit
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T: Any> LazyLoadColumn(
    list: LazyPagingItems<T>,
    key: (index: Int) -> Int,
    itemContent: @Composable LazyItemScope.(value: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(MySize.ScreenHorizontalPadding, 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (list.loadState.refresh == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }

        items(
            count = list.itemCount,
            key = key,
            itemContent = {index ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()) {
                    itemContent(index)
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