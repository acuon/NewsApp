package com.example.newsapp.presentation.commons

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

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
        block: suspend CoroutineScope.() -> T
    ): Job {
        return CoroutineScope(viewModelContext + dispatcher + exceptionHandler).launch {
            try {
                if (showLoading) {
                    postValue(Resource.Loading)
                }
                val result = block()
                Log.w(TAG, result.toString())
                postValue(Resource.Success(result))
            } catch (e: Exception) {
                Log.e(TAG, e.message ?: e.localizedMessage, e)
                postValue(createError(e))
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
                    throwable = throwable
                )
            }

            is java.net.SocketTimeoutException -> {
                Resource.Error(
                    message = "Connection timeout",
                    throwable = throwable
                )
            }

            else -> {
                Resource.Error(
                    message = throwable.message ?: "Unknown error occurred",
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