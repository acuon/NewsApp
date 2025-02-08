package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.NewsDto
import com.example.newsapp.presentation.ui.home.model.NewsArticle

interface NewsRepository {

    suspend fun fetchTopHeadlines(country: String = "us"): List<NewsArticle>?

    suspend fun getCachedHeadlines(): List<NewsArticle>

    suspend fun saveHeadlines(articles: List<NewsArticle>)

    suspend fun clearHeadlines()

    suspend fun fetchByQuery(query: String): NewsDto?
}