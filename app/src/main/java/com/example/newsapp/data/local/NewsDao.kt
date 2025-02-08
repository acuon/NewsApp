package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.presentation.ui.home.model.NewsArticle

@Dao
interface NewsDao {

    @Query("SELECT * FROM NewsDatabase")
    suspend fun getAllArticles(): List<NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Query("DELETE FROM NewsDatabase")
    suspend fun clearAllArticles()

}
