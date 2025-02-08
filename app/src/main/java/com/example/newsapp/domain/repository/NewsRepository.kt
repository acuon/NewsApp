package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.NewsDto

interface NewsRepository {

    suspend fun fetchTopHeadlines(country: String = "us"): NewsDto?

    suspend fun fetchByQuery(query: String): NewsDto?
}