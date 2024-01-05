package com.ymmbj.mz.lifcycler

import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.ymmbj.mz.ktx.myApplication
import com.ymmbj.mz.receiver.NetworkChangeReceiver

class NetworkLifecycleObserver : DefaultLifecycleObserver {

    private var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver()

    private var isRegister = false

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (isRegister) return
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        myApplication.registerReceiver(networkChangeReceiver, intentFilter)
        isRegister = true
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        isRegister = false
        myApplication.unregisterReceiver(networkChangeReceiver)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        owner.lifecycle.removeObserver(this)
    }


}