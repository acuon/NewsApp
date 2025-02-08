package com.example.newsapp.presentation.ui.home.model

import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.domain.models.ArticleDto
import com.example.newsapp.domain.models.NewsDto

data class NewsArticle(val image: String, val title: String)

fun ArticleDto.toNewsArticle(): NewsArticle {
    return NewsArticle("", "")
}