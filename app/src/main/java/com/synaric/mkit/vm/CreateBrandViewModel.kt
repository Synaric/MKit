package com.synaric.mkit.vm

import android.text.TextUtils
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synaric.art.util.ToastUtil
import com.synaric.mkit.R
import com.synaric.mkit.data.entity.Brand
import com.synaric.mkit.data.repo.BrandRepository
import kotlinx.coroutines.launch

class CreateBrandViewModel : ViewModel() {

    private val brandRepository = BrandRepository()

    val brand = mutableStateOf(Brand.createEmptyObject(), neverEqualPolicy())

    val showSimilarBrandDialog = mutableStateOf(false)

    fun createBrand(onCreateSuccess: () -> Unit) {
        val newBrand = brand.value
        if (TextUtils.isEmpty(newBrand.brand)) {
            ToastUtil.show(R.string.empty_brand)
            return
        }

        viewModelScope.launch {
            val similarBrandList = brandRepository.getSimilarBrandList(newBrand.brand)
            if (similarBrandList.isNotEmpty()) {
                // TODO 展示弹窗
                return@launch
            }

            brandRepository.insert(newBrand)
            onCreateSuccess()
        }
    }
}