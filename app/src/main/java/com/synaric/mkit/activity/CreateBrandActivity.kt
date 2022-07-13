package com.synaric.mkit.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.synaric.art.BaseActivity
import com.synaric.art.util.ToastUtil
import com.synaric.mkit.R
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
        DefaultSurface {
            Column {
                EditTextField(
                    value = model.brand.value.brand,
                    onValueChange = { model.brand.value.brand = it })

                EditTextField(
                    value = model.brand.value.brandLocale,
                    onValueChange = { model.brand.value.brandLocale = it })

                EditTextField(
                    value = model.brand.value.brandAlias,
                    onValueChange = { model.brand.value.brandAlias = it })

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
            singleLine = true)
    }
}