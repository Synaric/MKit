package com.synaric.mkit.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.synaric.mkit.R
import com.synaric.mkit.base.theme.MySize

@Composable
fun ConfirmDialog(
    show: MutableState<Boolean>,
    option: ConfirmDialogOption,
    onLeftButtonClick: () -> Unit = {},
    onMiddleButtonClick: () -> Unit = {},
    onRightButtonClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    if (show.value) {
        Dialog(
            onDismissRequest = {
                show.value = false
                onDismissRequest()
            }) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(MySize.ScreenHorizontalPadding, MySize.ScreenVerticalPadding)
            ) {
                Text(
                    text = option.title,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                )
                Text(
                    text = option.title,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 30.sp
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (option.style >= ConfirmDialogOption.StyleThree) {
                        Button(
                            onClick = {
                                show.value = false
                                onLeftButtonClick()
                            }) {
                            Text(text = option.leftButtonText)
                        }
                    }
                    if (option.style >= ConfirmDialogOption.StyleTwo) {
                        Button(
                            onClick = {
                                show.value = false
                                onMiddleButtonClick()
                            }) {
                            Text(text = option.middleButtonText)
                        }
                    }
                    if (option.style >= ConfirmDialogOption.StyleOne) {
                        Button(
                            onClick = {
                                show.value = false
                                onRightButtonClick()
                            }) {
                            Text(text = option.rightButtonText)
                        }
                    }
                }
            }
        }
    }
}

data class ConfirmDialogOption(

    val title: String,
    val content: String,
    val leftButtonText: String = "",
    val middleButtonText: String = "",
    val rightButtonText: String = "",
    val style: Int = 0
) {

    companion object {

        const val StyleOne = 0
        const val StyleTwo = 1
        const val StyleThree = 2

        fun createDefault(context: Context, content: String): ConfirmDialogOption {
            return ConfirmDialogOption(
                context.getString(R.string.attention),
                content,
                "",
                context.getString(R.string.cancel),
                context.getString(R.string.ok)
            )
        }
    }
}