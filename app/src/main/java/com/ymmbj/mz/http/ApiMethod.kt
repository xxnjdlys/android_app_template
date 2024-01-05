package com.ymmbj.mz.http

import com.ymmbj.mz.model.InitConfig
import com.ymmbj.mz.model.UserInfo
import retrofit2.http.*

interface ApiMethod {

    @FormUrlEncoded
    @POST("/api/user/wxlogin")
    suspend fun wxlogin(@Field("info") info: String?): ApiResult<UserInfo>

    @GET("/api/user/info")
    suspend fun refreshUserInfo(@Query("uid") uid: String): ApiResult<UserInfo>

    @GET("/api/setting/config")
    suspend fun getInitConfig(): ApiResult<InitConfig>

}