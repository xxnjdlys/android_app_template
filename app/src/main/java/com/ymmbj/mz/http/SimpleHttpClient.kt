package com.ymmbj.mz.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class SimpleHttpClient {

    companion object {
        val sInstance: SimpleHttpClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SimpleHttpClient()
        }
    }

    suspend fun get(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        return execute(Request.Builder().url(url).build())
    }

    fun getSync(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        return getSync(url,Headers.Builder().build())
    }

    fun getSync(url: String?,header:Headers): String? {
        if (url.isNullOrEmpty()) return null
        val response = ApiClient.getInstance().okHttpClient.newCall(Request.Builder().url(url)
            .headers(header).build()).execute()
        return if (response.isSuccessful) {
            response.body()?.string()
        } else {
            null
        }
    }

    suspend fun post(url: String?, body: RequestBody): String? {
        if (url.isNullOrEmpty()) return null
        return execute(Request.Builder().url(url).post(body).build())
    }

    private suspend fun execute(request: Request): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiClient.getInstance().okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    return@withContext response.body()?.string()
                } else {
                    return@withContext null
                }
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

    fun getSyncHttpResponse(url: String): Response? {
        val response = ApiClient.getInstance().okHttpClient.newCall(Request.Builder().url(url)
            .build()).execute()
        return if (response.isSuccessful) {
            response
        } else {
            null
        }
    }


}