package com.example.newsapp.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.model.data.local.repositories.NewsRepositoryImpl
import com.example.newsapp.util.Constants.DEFAULT_LOADING_DURATION_IN_MILLIS
import com.example.newsapp.util.Constants.PAGE_SIZE
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl
) : ViewModel() {

    private var currentPage = 1

    var newsList = mutableStateOf<List<NewsEntity>>(listOf())
    var loadError = mutableStateOf(false)
    var isLoadingInitialNews = mutableStateOf(false)
    var paginationEndReached = mutableStateOf(false)
    var noOfflineNews = mutableStateOf(false)
    var refreshing = mutableStateOf(false)

    init {
        loadNewsListPaginated()
    }

    fun refreshNews() {
        loadError.value = false
        refreshing.value = true
        currentPage = 1
        loadNewsListPaginated()
    }

    fun loadDbSavedNews() {
        viewModelScope.launch {
            val dbSavedNews = repository.getDbSavedNewsList()
            noOfflineNews.value = dbSavedNews.isEmpty()
            newsList.value = dbSavedNews
        }
    }

    fun loadNewsListPaginated() {
        viewModelScope.launch {
            // If the news are loaded initially, display a circular progress indicator and give a little time to the user
            // to see the loading process
            if (newsList.value.isEmpty()) {
                isLoadingInitialNews.value = true
                delay(DEFAULT_LOADING_DURATION_IN_MILLIS)
            }

            val newsResponseWithTotalResults = repository.getNewsResponseWithTotalResults(page = currentPage)
            val newsEntitiesList = newsResponseWithTotalResults.data?.first
            val totalResults = newsResponseWithTotalResults.data?.second

            when (newsResponseWithTotalResults) {
                is Resource.Success -> {
                    paginationEndReached.value = currentPage * PAGE_SIZE >= (totalResults ?: PAGE_SIZE)
                    currentPage++
                    loadError.value = false
                    // If the news are loaded initially or are being refreshed and the response is successful,
                    // the database is updated, so the old data are deleted
                    if (isLoadingInitialNews.value || refreshing.value) {
                        if (newsEntitiesList != null) {
                            repository.deleteDb()
                        }
                        isLoadingInitialNews.value = false
                        refreshing.value = false
                    }
                    if (newsEntitiesList != null) {
                        repository.insertNewsEntitiesToDb(newsEntitiesList)
                        newsList.value = repository.getDbSavedNewsList()
                    }
                }

                is Resource.Error -> {
                    // If the news cannot be loaded, give a little time to the user to see the circular progress indicator
                    // before the error message is displayed
                    delay(DEFAULT_LOADING_DURATION_IN_MILLIS)
                    loadError.value = true
                    Log.e("Failed to load News Articles.", "Reason: ${ newsResponseWithTotalResults.message }")
                    isLoadingInitialNews.value = false
                    refreshing.value = false
                }
            }
        }
    }
}