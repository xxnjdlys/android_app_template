package com.ymmbj.mz.http

class ApiException(val status: Int, msg: String) : Exception(msg)