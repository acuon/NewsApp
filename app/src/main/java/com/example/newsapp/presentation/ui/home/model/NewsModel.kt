package com.example.newsapp.presentation.ui.home.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.domain.models.ArticleDto
import kotlinx.parcelize.Parcelize

@Entity(tableName = "NewsDatabase")
@Parcelize
data class NewsArticle(
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "sourceName")
    val sourceName: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String?,
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String?,
    @ColumnInfo(name = "content")
    val content: String?
) : Parcelable

fun ArticleDto.toNewsArticle(): NewsArticle {
    return NewsArticle(
        title = this.title.toString(),
        sourceName = this.source?.name,
        author = this.author,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}