package com.synaric.mkit.screen

import android.Manifest
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.synaric.mkit.vm.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyScreen(
    model: MainViewModel,
    onMyScreenStoragePermissionResult: (granted: Boolean, type: Int) -> Unit
) {
    val inStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) { granted ->
        onMyScreenStoragePermissionResult(granted, 0)
    }

    val outStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) { granted ->
        onMyScreenStoragePermissionResult(granted, 1)
    }

    Button(onClick = { inStoragePermissionState.launchPermissionRequest() }) {
        Text(text = "导入")
    }

    Button(onClick = { outStoragePermissionState.launchPermissionRequest() }) {
        Text(text = "导出")
    }
}