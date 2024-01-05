package com.ymmbj.mz.ktx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.ymmbj.mz.http.ApiException
import com.ymmbj.mz.http.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> ViewModel.request(requestBlock: suspend () -> ApiResult<T>, success: (T) -> Unit, error: (Throwable?) -> Unit) {
    viewModelScope.request(requestBlock, success, error)
}

fun <T> ViewModel.request(requestBlock: suspend () -> ApiResult<T>, success: (T) -> Unit, error: (Throwable?) -> Unit, apiException: (ApiException) -> Unit) {
    viewModelScope.request(requestBlock, success, error, apiException)
}

fun <T> LifecycleOwner.request(requestBlock: suspend () -> ApiResult<T>, success: (T) -> Unit, error: (Throwable?) -> Unit) {
    lifecycleScope.request(requestBlock, success, error)
}

fun <T> LifecycleOwner.request(requestBlock: suspend () -> ApiResult<T>, success: (T) -> Unit, error: (Throwable?) -> Unit, apiException: (ApiException) -> Unit) {
    lifecycleScope.request(requestBlock, success, error, apiException)
}

fun <T> CoroutineScope.request(
    requestBlock: suspend () -> ApiResult<T>,
    onSuccess: (T) -> Unit,
    onError: (Throwable?) -> Unit,
    apiException: ((ApiException) -> Unit)? = null
) {
    launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                requestBlock()
            }
        }.onSuccess { result ->
            kotlin.runCatching {
                val data = result.data()
                onSuccess(data)
            }.onFailure { exception ->
                // Handle any exceptions that occurred in onSuccess
                onError(exception)
            }
        }.onFailure { exception ->
            // Handle the failure of the requestBlock
            if (exception is ApiException && apiException != null) {
                apiException(exception)
            } else {
                // Handle other types of exceptions
                onError(exception)
            }
        }
    }
}
