package com.example.newsapp.presentation.ui.home.model

import com.example.newsapp.domain.models.ArticleDto

data class NewsArticle(
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

fun ArticleDto.toNewsArticle(): NewsArticle {
    return NewsArticle(
        sourceName = this.source?.name,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}