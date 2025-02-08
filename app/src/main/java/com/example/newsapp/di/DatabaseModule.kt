package com.example.newsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCachedMoviesDao(database: NewsDatabase): NewsDao {
        return database.getNewsDao()
    }

    @Provides
    @Singleton
    fun provideMoviesDatabase(@ApplicationContext application: Context): NewsDatabase {
        return Room.databaseBuilder(
            application,
            NewsDatabase::class.java,
            "NewsDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}