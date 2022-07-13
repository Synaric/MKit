package com.synaric.art.util

import android.content.Context
import android.widget.Toast
import com.synaric.art.BaseApplication

class ToastUtil {

    companion object {

        fun show(msg: String?) {
            Toast.makeText(BaseApplication.Instance, msg, Toast.LENGTH_SHORT).show()
        }

        fun show(resId: Int) {
            Toast.makeText(BaseApplication.Instance, resId, Toast.LENGTH_SHORT).show()
        }
    }
}