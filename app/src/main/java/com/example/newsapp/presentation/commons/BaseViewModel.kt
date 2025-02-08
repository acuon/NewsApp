package com.example.newsapp.presentation.commons

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {
    private val job = SupervisorJob()
    private val viewModelContext: CoroutineContext = Dispatchers.Main + job

    companion object {
        private const val TAG = "BaseViewModel"
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG, "${throwable.message}", throwable)
    }

    protected fun <T> MutableLiveData<Resource<T>>.safeLaunch(
        dispatcher: CoroutineContext = Dispatchers.IO,
        showLoading: Boolean = true,
        fetchFromNetwork: suspend CoroutineScope.() -> T,
        fetchFromCache: (suspend CoroutineScope.() -> T)? = null,
        saveToCache: (suspend CoroutineScope.(T) -> Unit)? = null,
        clearCache: (suspend CoroutineScope.() -> Unit)? = null
    ): Job {
        return CoroutineScope(viewModelContext + dispatcher + exceptionHandler).launch {
            try {
                if (showLoading) {
                    postValue(Resource.Loading)
                }
                try {
                    val networkResult = fetchFromNetwork()
                    clearCache?.invoke(this)
                    saveToCache?.invoke(this, networkResult)
                    postValue(Resource.Success(networkResult, DataSource.NETWORK))
                } catch (e: Exception) {
                    Log.e(TAG, "Network error: ${e.message}", e)
                    if (fetchFromCache != null) {
                        try {
                            val cachedResult = fetchFromCache()
                            postValue(createCacheResponse(cachedResult, e))
                        } catch (cacheError: Exception) {
                            Log.e(TAG, "Cache error: ${cacheError.message}", cacheError)
                            postValue(createError(e))
                        }
                    } else {
                        postValue(createError(e))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.localizedMessage, e)
                postValue(createError(e))
            }
        }
    }

    private fun <T> createCacheResponse(data: T, throwable: Throwable): Resource<T> {
        return when (throwable) {
            is retrofit2.HttpException -> {
                Resource.Success(
                    data,
                    DataSource.CACHE,
                    message = throwable.message ?: "Network Error",
                    code = throwable.code(),
                    throwable = throwable
                )
            }

            is java.net.UnknownHostException -> {
                Resource.Success(
                    data,
                    DataSource.CACHE,
                    message = "No internet connection",
                    code = 0,
                    throwable = throwable
                )
            }

            is java.net.SocketTimeoutException -> {
                Resource.Success(
                    data,
                    DataSource.CACHE,
                    message = "Connection timeout",
                    code = 1,
                    throwable = throwable
                )
            }

            else -> {
                Resource.Success(
                    data,
                    DataSource.CACHE,
                    message = throwable.message ?: "Unknown error occurred",
                    code = 2,
                    throwable = throwable
                )
            }
        }
    }

    private fun <T> createError(throwable: Throwable): Resource<T> {
        return when (throwable) {
            is retrofit2.HttpException -> {
                Resource.Error(
                    message = throwable.message ?: "Network Error",
                    code = throwable.code(),
                    throwable = throwable
                )
            }

            is java.net.UnknownHostException -> {
                Resource.Error(
                    message = "No internet connection",
                    code = 0,
                    throwable = throwable
                )
            }

            is java.net.SocketTimeoutException -> {
                Resource.Error(
                    message = "Connection timeout",
                    code = 1,
                    throwable = throwable
                )
            }

            else -> {
                Resource.Error(
                    message = throwable.message ?: "Unknown error occurred",
                    code = 2,
                    throwable = throwable
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}