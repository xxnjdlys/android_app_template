package com.ymmbj.mz.http

class ApiResult<T> {

    var status: Int = 0
    var data: T? = null
    var msg: String = ""

    fun data(): T {
        when (status) {
            1 -> {//成功
                return data!!
            }
        }
        throw ApiException(status, msg)
    }


}