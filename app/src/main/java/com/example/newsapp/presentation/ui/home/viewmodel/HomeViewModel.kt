package com.example.newsapp.presentation.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.presentation.commons.BaseViewModel
import com.example.newsapp.presentation.commons.Resource
import com.example.newsapp.presentation.ui.home.model.NewsArticle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    BaseViewModel() {

    private val _trendingResponse = MutableLiveData<Resource<List<NewsArticle?>?>>()
    val trendingResponse: LiveData<Resource<List<NewsArticle?>?>> = _trendingResponse

    init {
        fetchTrendingNews()
    }

    fun fetchTrendingNews() {
        _trendingResponse.safeLaunch(
            fetchFromNetwork = {
                newsRepository.fetchTopHeadlines("us")
            },
            fetchFromCache = {
                newsRepository.getCachedHeadlines()
            },
            saveToCache = { articles ->
                articles?.let { newsRepository.saveHeadlines(it.filterNotNull()) }
            },
            clearCache = {
                newsRepository.clearHeadlines()
            }
        )
    }
}