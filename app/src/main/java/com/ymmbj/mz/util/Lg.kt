package com.ymmbj.mz.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ymmbj.mz.BuildConfig


/**
 * 打印 android log 的帮助类.
 */
@Suppress("unused")
object Lg {
    private var isLogEnable = BuildConfig.DEBUG
    private const val TAG = "MasterZhou"

    fun enableDebug(b: Boolean) {
        isLogEnable = b
    }

    fun d(log: String?) {
        if (isLogEnable) {
            Log.d(TAG, log!!)
        }
    }

    fun v(log: String?) {
        if (isLogEnable) {
            Log.v(TAG, log!!)
        }
    }

    fun e(log: String?) {
        if (isLogEnable) {
            Log.e(TAG, log!!)
        }
    }

    fun e(t: Throwable?) {
        if (isLogEnable) {
            Log.e(TAG, t?.message, t)
        }
    }

    fun w(log: String?) {
        if (isLogEnable) {
            Log.w(TAG, log!!)
        }
    }

    fun i(log: String?) {
        if (isLogEnable) {
            Log.i(TAG, log!!)
        }
    }

    fun d(tag: String?, log: String?) {
        if (isLogEnable) {
            Log.d(tag, log!!)
        }
    }

    fun v(tag: String?, log: String?) {
        if (isLogEnable) {
            Log.v(tag, log!!)
        }
    }

    fun e(tag: String?, log: String?) {
        if (isLogEnable) {
            Log.e(tag, log!!)
        }
    }

    fun w(tag: String?, log: String?) {
        if (isLogEnable) {
            Log.w(tag, log!!)
        }
    }

    fun i(tag: String?, log: String?) {
        if (isLogEnable) {
            Log.i(tag, log!!)
        }
    }

    fun debugToast(context: Context?, msg: String?) {
        if (isLogEnable) {
            try {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            } catch (ignore: Exception) {
            }
        }
    }
}