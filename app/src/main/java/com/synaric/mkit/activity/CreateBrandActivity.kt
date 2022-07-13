package com.synaric.mkit.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.synaric.art.BaseActivity
import com.synaric.art.util.ToastUtil
import com.synaric.mkit.R
import com.synaric.mkit.base.theme.MySize
import com.synaric.mkit.base.view.DefaultSurface
import com.synaric.mkit.vm.CreateBrandViewModel

class CreateBrandActivity : BaseActivity() {

    private val model: CreateBrandViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateView()
        }
    }

    private val onCreateSuccess = {
        ToastUtil.show(R.string.create_success)
        finish()
    }

    @Composable
    fun CreateView() {
        val brand = remember { model.brand.value }
        DefaultSurface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MySize.ScreenHorizontalPadding, 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EditTextField(
                    value = brand.brand,
                    onValueChange = {
                        brand.brand = it
                        model.brand.value = brand
                    })

                Spacer(modifier = Modifier.height(20.dp))

                EditTextField(
                    value = model.brand.value.brandLocale,
                    onValueChange = {
                        brand.brandLocale = it
                        model.brand.value = brand
                    })

                Spacer(modifier = Modifier.height(20.dp))

                EditTextField(
                    value = model.brand.value.brandAlias,
                    onValueChange = {
                        brand.brandAlias = it
                        model.brand.value = brand
                    })

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { model.createBrand(this@CreateBrandActivity.onCreateSuccess) }) {
                    Text(text = "创建")
                }
            }

            if (model.showSimilarBrandDialog.value) {
                Dialog(onDismissRequest = { model.showSimilarBrandDialog.value = false }) {
                    Text(text = "提示")
                }
            }
        }
    }

    @Composable
    fun EditTextField(
        value: String,
        onValueChange: (s: String) -> Unit
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "标签")
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            )
        )
    }
}