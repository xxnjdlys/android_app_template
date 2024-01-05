package com.ymmbj.mz.http

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.ymmbj.mz.BuildConfig
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiClient {

    companion object {

        const val RELEASE_API = "https://xxx.xxx.xxx"  // fixme 请设置接口请求的地址
        const val TEST_API = "https://xxx.xxx.xxx"     // fixme 请设置接口请求的地址
        const val DEV_API = "https://xxx.xxx.xxx"      // fixme 请设置接口请求的地址

        const val USER_AGENT = "YMMBJ"

        private var sInstance: ApiClient? = null
        fun getInstance(): ApiClient {
            if (sInstance == null) {
                synchronized(ApiClient::class.java) {
                    if (sInstance == null) {
                        sInstance = ApiClient()
                    }
                }
            }
            return sInstance!!
        }
    }

    lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var mApiMethod: ApiMethod


    private var sCurrentFlavor: String = ""
    private var sCurrentLang: String = ""
    private var sVersionCode: Int = 0
    private var sPhoneType: String = ""
    private var sAndroidVer: String = ""
    private var sVersionName: String = ""


    fun init(context: Context) {
        initDefaultParams()
        val okHttpClientBuilder = OkHttpClient.Builder()
        val trustAllCerts = @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

        var sslSocketFactory: SSLSocketFactory? = null
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf<TrustManager>(trustAllCerts), SecureRandom())
            sslSocketFactory = sslContext.socketFactory
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        okHttpClientBuilder.addInterceptor(mDefaultInterceptor)
        okHttpClientBuilder.addInterceptor(UserAgentInterceptor())
        if (sslSocketFactory != null) {
            okHttpClientBuilder.sslSocketFactory(
                sslSocketFactory, trustAllCerts
            )
        }
        okHttpClientBuilder.cache(Cache(File(context.cacheDir, "okcache"), 1024 * 1024 * 10))
        okHttpClientBuilder.hostnameVerifier { hostname, session -> true }
        okHttpClient = okHttpClientBuilder.build()

        retrofit = Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).baseUrl(getBaseUrl()).build()

        mApiMethod = retrofit.create(ApiMethod::class.java)
    }

    private fun getBaseUrl():String {
        var api = ""
        if (BuildConfig.DEBUG) {
            api = TEST_API
        }
        if (BuildConfig.FLAVOR == "dev") {
            api = DEV_API
        }

        if (TextUtils.isEmpty(api)) {
            api =  RELEASE_API
        }

        if (TextUtils.isEmpty(api)) {
            throw Exception("请先设置接口请求地址.")
        }
        return api
    }

    fun getApiMethod(): ApiMethod {
        return mApiMethod
    }

    private fun initDefaultParams() {
        sCurrentFlavor = BuildConfig.FLAVOR
        sVersionCode = BuildConfig.VERSION_CODE
        sVersionName = BuildConfig.VERSION_NAME
        sPhoneType = android.os.Build.MODEL
        sAndroidVer = android.os.Build.VERSION.RELEASE
        sCurrentLang = Locale.getDefault().language
    }

    private val mDefaultInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val urlBuilder = originalHttpUrl.newBuilder()
            .addQueryParameter("phonetype", sPhoneType)
            .addQueryParameter("osver", sAndroidVer)
            .addQueryParameter("f", sCurrentFlavor)
            .addQueryParameter("v", "$sVersionCode")
            .addQueryParameter("dev", "android")
            .addQueryParameter("vn", sVersionName)
            .addQueryParameter("lang", sCurrentLang)

        val requestBuilder: Request.Builder = originalRequest.newBuilder().url(urlBuilder.build())
            .addHeader("timezone", TimeZone.getDefault().id)
            .addHeader("lang", sCurrentLang)

        chain.proceed(requestBuilder.build())
    }

    private class UserAgentInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            if (originalRequest.header("User-Agent") != null) {
                return chain.proceed(originalRequest)
            }

            val customRequest =
                originalRequest.newBuilder().header("User-Agent", USER_AGENT)
                    .build()
            val response: Response = try {
                chain.proceed(customRequest)
            } catch (ignore: Throwable) {
                if (ignore is IOException) {
                    throw ignore
                } else {
                    Response.Builder().request(customRequest).protocol(Protocol.HTTP_1_1)
                        .code(404).message("not found").build()
                }
            }
            return response
        }
    }


}