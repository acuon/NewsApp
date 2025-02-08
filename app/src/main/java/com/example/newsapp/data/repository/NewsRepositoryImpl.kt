package com.example.newsapp.data.repository

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.domain.models.NewsDto
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import com.example.newsapp.presentation.ui.home.model.toNewsArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun fetchTopHeadlines(country: String): List<NewsArticle> {
        return newsApi.fetchTopHeadlines(country)?.articles?.map { it.toNewsArticle() }
            ?: throw Exception("Failed to fetch headlines")
    }

    override suspend fun getCachedHeadlines(): List<NewsArticle> {
        return newsDao.getAllArticles()
    }

    override suspend fun saveHeadlines(articles: List<NewsArticle>) {
        newsDao.insertArticles(articles)
    }

    override suspend fun clearHeadlines() {
        newsDao.clearAllArticles()
    }

    override suspend fun fetchByQuery(query: String): NewsDto? {
        return newsApi.fetchByQuery(query)
    }
}