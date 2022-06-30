package com.synaric.art

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import com.synaric.art.util.AppLog
import com.synaric.art.util.FileUtil
import com.synaric.art.util.FileUtil.Companion.saveFileToSD

abstract class BaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            data?.let {
                AppLog.d(this, data.getParcelableExtra<Uri>(DocumentsContract.EXTRA_INITIAL_URI).toString())
            }
        }
    }
}