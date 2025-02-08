package com.example.newsapp.data.remote

import com.example.newsapp.domain.models.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun fetchByQuery(@Query("q") query: String): NewsDto?

    @GET("top-headlines")
    suspend fun fetchTopHeadlines(@Query("country") country: String): NewsDto?

}