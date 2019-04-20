package com.asj.mpdemo.util

import android.content.Context
import android.widget.Toast
import com.asj.mpdemo.R

class Utility {
    companion object {
        fun showToast(ctx: Context, msg: String) {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
