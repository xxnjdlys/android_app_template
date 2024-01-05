package com.ymmbj.mz.ktx

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ymmbj.mz.util.Lg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 同步存放数据
 */
fun <T> DataStore<Preferences>.putData(key: String, value: T) {
    if (value == null) return
    runBlocking {
        when (value) {
            is String -> {
                putString(key, value)
            }
            is Int -> {
                putInt(key, value)
            }
            is Long -> {
                putLong(key, value)
            }
            is Float -> {
                putFloat(key, value)
            }
            is Double -> {
                putDouble(key, value)
            }
            is Boolean -> {
                putBoolean(key, value)
            }
        }
    }
}

/**
 * 异步存放数据
 */
fun <T> DataStore<Preferences>.putDataAsync(key: String, value: T) {
    CoroutineScope(Dispatchers.IO).launch {
        when (value) {
            is String -> {
                putString(key, value)
            }
            is Int -> {
                putInt(key, value)
            }
            is Long -> {
                putLong(key, value)
            }
            is Float -> {
                putFloat(key, value)
            }
            is Double -> {
                putDouble(key, value)
            }
            is Boolean -> {
                putBoolean(key, value)
            }
        }
    }
}

/**
 * 取出数据
 */
fun <T> DataStore<Preferences>.getData(key: String, defaultValue: T): T {
    val data = when (defaultValue) {
        is String -> {
            getString(key, defaultValue)
        }
        is Int -> {
            getInt(key, defaultValue)
        }
        is Long -> {
            getLong(key, defaultValue)
        }
        is Float -> {
            getFloat(key, defaultValue)
        }
        is Double -> {
            getDouble(key, defaultValue)
        }
        is Boolean -> {
            getBoolean(key, defaultValue)
        }
        else -> {
            throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
    }
    return data as T
}

/**
 * 存放String数据
 */
private suspend fun DataStore<Preferences>.putString(key: String, value: String) {
    edit {
        it[stringPreferencesKey(key)] = value
    }
}

/**
 * 存放Int数据
 */
private suspend fun DataStore<Preferences>.putInt(key: String, value: Int) {
    edit {
        it[intPreferencesKey(key)] = value
    }
}

/**
 * 存放Long数据
 */
private suspend fun DataStore<Preferences>.putLong(key: String, value: Long) {
    edit {
        it[longPreferencesKey(key)] = value
    }
}

/**
 * 存放Float数据
 */
private suspend fun DataStore<Preferences>.putFloat(key: String, value: Float) {
    edit {
        it[floatPreferencesKey(key)] = value
    }
}

/**
 * 存放Double数据
 */
private suspend fun DataStore<Preferences>.putDouble(key: String, value: Double) {
    edit {
        it[doublePreferencesKey(key)] = value
    }
}

/**
 * 存放Boolean数据
 */
private suspend fun DataStore<Preferences>.putBoolean(key: String, value: Boolean) {
    edit {
        it[booleanPreferencesKey(key)] = value
    }
}

/**
 * 取出String数据
 */
private fun DataStore<Preferences>.getString(key: String, default: String? = null): String = runBlocking {
    return@runBlocking dataCatch().map {
        it[stringPreferencesKey(key)] ?: default
    }.first()!!
}

/**
 * 取出Int数据
 */
private fun DataStore<Preferences>.getInt(key: String, default: Int = 0): Int = runBlocking {
    return@runBlocking dataCatch().map {
        it[intPreferencesKey(key)] ?: default
    }.first()
}

/**
 * 取出Long数据
 */
private fun DataStore<Preferences>.getLong(key: String, default: Long = 0): Long = runBlocking {
    return@runBlocking dataCatch().map {
        it[longPreferencesKey(key)] ?: default
    }.first()
}

/**
 * 取出Float数据
 */
private fun DataStore<Preferences>.getFloat(key: String, default: Float = 0.0f): Float = runBlocking {
    return@runBlocking dataCatch().map {
        it[floatPreferencesKey(key)] ?: default
    }.first()
}

/**
 * 取出Double数据
 */
private fun DataStore<Preferences>.getDouble(key: String, default: Double = 0.00): Double = runBlocking {
    return@runBlocking dataCatch().map {
        it[doublePreferencesKey(key)] ?: default
    }.first()
}

/**
 * 取出Boolean数据
 */
private fun DataStore<Preferences>.getBoolean(key: String, default: Boolean = false): Boolean = runBlocking {
    return@runBlocking dataCatch().map {
        it[booleanPreferencesKey(key)] ?: default
    }.first()
}

private fun DataStore<Preferences>.dataCatch() = data.catch {
    it.printStackTrace()
    Lg.d("data store err ${it.message}")
}