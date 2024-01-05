package com.ymmbj.mz.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.ymmbj.mz.util.Lg

class NetworkChangeReceiver : BroadcastReceiver() {
    companion object {
        var mLastWifiName = ""
        val networkChange: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        val wifiName = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO)
        if (!TextUtils.isEmpty(wifiName) && (TextUtils.isEmpty(mLastWifiName) || mLastWifiName != wifiName)) {
            Lg.d( "NetworkChangedReceiver:onReceive:wifiName $wifiName")
            mLastWifiName = wifiName!!

            networkChange.value = wifiName
        }
    }

}