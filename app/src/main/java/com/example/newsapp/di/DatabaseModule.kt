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

    /**
     * Provides a singleton instance of NewsDao for accessing cached news data.
     *
     * This function returns a singleton NewsDao instance, which allows querying, inserting,
     * updating, and deleting news records in the Room database. The singleton ensures a single
     * instance is used throughout the app.
     *
     * @param database The initialized NewsDatabase instance for accessing the NewsDao.
     *
     * @return The NewsDao instance for interacting with news data in the database.
     */
    @Provides
    @Singleton
    fun provideNewsDao(database: NewsDatabase): NewsDao {
        return database.getNewsDao()
    }

    /**
     * Provides a singleton instance of NewsDatabase for persisting news data.
     *
     * This function initializes the NewsDatabase using Room's database builder, allowing
     * local persistence of news data. It supports destructive migration in case of schema changes.
     *
     * @param application The application context to initialize the database.
     *
     * @return The initialized NewsDatabase instance for accessing news data.
     */
    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext application: Context): NewsDatabase {
        return Room.databaseBuilder(
            application,
            NewsDatabase::class.java,
            "NewsDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}