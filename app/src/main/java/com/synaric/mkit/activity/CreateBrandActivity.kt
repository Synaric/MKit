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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synaric.art.BaseActivity
import com.synaric.art.util.ToastUtil
import com.synaric.mkit.R
import com.synaric.mkit.base.theme.MySize
import com.synaric.mkit.base.view.DefaultSurface
import com.synaric.mkit.view.ConfirmDialog
import com.synaric.mkit.view.ConfirmDialogOption
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
                    },
                    stringResource(R.string.hint_brand)
                )

                Spacer(modifier = Modifier.height(20.dp))

                EditTextField(
                    value = model.brand.value.brandLocale,
                    onValueChange = {
                        brand.brandLocale = it
                        model.brand.value = brand
                    },
                    stringResource(R.string.hint_brand_locale)
                )

                Spacer(modifier = Modifier.height(20.dp))

                EditTextField(
                    value = model.brand.value.brandAlias,
                    onValueChange = {
                        brand.brandAlias = it
                        model.brand.value = brand
                    },
                    stringResource(R.string.hint_brand_alias)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { model.createBrand(this@CreateBrandActivity.onCreateSuccess) }) {
                    Text(text = stringResource(R.string.create))
                }
            }

            if (model.showSimilarBrandDialog.value) {
                ConfirmDialog(
                    show = model.showSimilarBrandDialog,
                    option = ConfirmDialogOption.createDefault(this, "")
                )
            }
        }
    }

    @Composable
    fun EditTextField(
        value: String,
        onValueChange: (s: String) -> Unit,
        label: String
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = label,
                    fontSize = 10.sp
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            )
        )
    }
}