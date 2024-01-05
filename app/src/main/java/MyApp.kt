package com.ymmbj.mz

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.bumptech.glide.Glide
import com.ymmbj.mz.common.Constants
import com.ymmbj.mz.http.ApiClient
import com.ymmbj.mz.ktx.dataStore
import com.ymmbj.mz.ktx.getData
import com.ymmbj.mz.util.Lg

class MyApp : MultiDexApplication() {

    companion object {
        lateinit var sInstance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        init()
    }

    private fun init() {
//        val processName = getProcessName(this, android.os.Process.myPid())
//        if (!TextUtils.isEmpty(processName) && processName.equals(packageName)) {
//            StatUtils.preInit(this)
//        }
        agreedPrivacy()
    }

    private fun agreedPrivacy() {
        val isAgreed = dataStore.getData(Constants.StoreKey.PRIVACY_AGREEMENT, false)
        if (isAgreed) {
            this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        }
        ApiClient.getInstance().init(this)
        Lg.enableDebug(BuildConfig.DEBUG)
    }

    private fun getProcessName(cxt: Context, pid: Int): String? {
        val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return null
        for (processInfo in runningApps) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return null
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackGround = true
            isBackGroundTime = System.currentTimeMillis()
            Glide.get(this).clearMemory()
            Lg.d("onTrimMemory")
        }
        Glide.get(this).trimMemory(level)
    }

    private var isBackGround = false
    private var isBackGroundTime: Long = 0

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        }

        override fun onActivityStarted(activity: Activity) {
            if (isBackGround) {
                isBackGround = false
                if (System.currentTimeMillis() - isBackGroundTime < 5000) {
                    return
                }
//                if (activity.javaClass == WakeUpActivity::class.java) {
//                    return
//                }
//                if (AdConfig.getAdConfig()?.mWakeAd?.check() == true) {
//                    val intent = Intent(applicationContext, WakeUpActivity::class.java)
//                    activity.startActivity(intent)
//                }
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

    }


}