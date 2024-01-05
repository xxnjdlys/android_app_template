package com.ymmbj.mz.ktx

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ymmbj.mz.MyApp
import com.ymmbj.mz.util.ThreadUtil
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher


// 创建DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "YMMBJ"
)

val myApplication: MyApp = MyApp.sInstance

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.showToast(@StringRes msg: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun getThreadCoroutineScope(name: String): CoroutineScope = CoroutineScope(SupervisorJob() + ThreadUtil.MyThreadPoolExecutor.asCoroutineDispatcher() + CoroutineName(name))

fun Int.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
    )
}

fun Int.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            context.resources.displayMetrics
    )
}

fun Int.getColorCompat(context: Context): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.resources.getColor(this, context.theme)
    } else {
        @Suppress("DEPRECATION")
        context.resources.getColor(this)
    }
}