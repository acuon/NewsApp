package com.example.newsapp.presentation.commons

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(
        val data: T,
        val source: DataSource = DataSource.NETWORK,
        val message: String? = null,
        val code: Int? = null,
        val throwable: Throwable? = null,
    ) : Resource<T>()

    data class Error<T>(
        val message: String,
        val code: Int? = null,
        val throwable: Throwable? = null,
        val data: T? = null
    ) : Resource<T>()
}

enum class DataSource {
    NETWORK,
    CACHE
}