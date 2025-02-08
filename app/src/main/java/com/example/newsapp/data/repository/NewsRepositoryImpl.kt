package com.example.newsapp.data.repository

import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.domain.models.NewsDto
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun fetchTopHeadlines(country: String): NewsDto? {
        return newsApi.fetchTopHeadlines(country)
    }

    override suspend fun fetchByQuery(query: String): NewsDto? {
        return newsApi.fetchByQuery(query)
    }
}