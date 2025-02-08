package com.example.newsapp.di

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.pref.SharedPrefs
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideNewsRepo(
        newsApi: NewsApi,
        newsDao: NewsDao
    ): NewsRepository = NewsRepositoryImpl(newsApi, newsDao)

    /**
     * Provides a Retrofit instance with a dynamically configurable base URL.
     * The base URL is fetched from SharedPreferences, allowing it to be updated based on remote configuration or network calls.
     * For improved security, consider storing the base URL in EncryptedSharedPreferences instead of regular SharedPreferences.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, prefs: SharedPrefs): Retrofit {
        val baseUrl: String = prefs.baseUrl.takeIf { !it.isNullOrEmpty() } ?: BuildConfig.BASE_URL
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60_000, TimeUnit.MILLISECONDS)
            .writeTimeout(60_000, TimeUnit.MILLISECONDS)
            .readTimeout(60_000, TimeUnit.MILLISECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
        return okHttpClient.build()
    }

    /**
     * Provides an interceptor that adds an Authorization header with a dynamically configurable API key.
     * The API key is retrieved from SharedPreferences, allowing it to be updated based on remote configurations or network calls.
     * For improved security, consider storing the base URL in EncryptedSharedPreferences instead of regular SharedPreferences.
     * If the API key is not found in SharedPreferences, the default API key from BuildConfig is used.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(prefs: SharedPrefs): Interceptor {
        return Interceptor { chain ->
            val token: String = prefs.apiKey.takeIf { !it.isNullOrEmpty() } ?: BuildConfig.API_KEY
            val requestBuilder = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestBuilder)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

}