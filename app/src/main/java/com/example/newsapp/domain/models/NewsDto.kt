package com.example.newsapp.domain.models

import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("status") val status: String? = null,
    @SerializedName("totalResults") val totalResults: Int? = null,
    @SerializedName("articles") val articles: List<ArticleDto>? = null
)

data class ArticleDto(
    @SerializedName("source") val source: SourceDto? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("urlToImage") val urlToImage: String? = null,
    @SerializedName("publishedAt") val publishedAt: String? = null,
    @SerializedName("content") val content: String? = null
)

data class SourceDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null
)
