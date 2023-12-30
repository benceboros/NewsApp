package com.example.newsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.entities.NewsEntity
import com.example.newsapp.data.local.repositories.NewsRepositoryImpl
import com.example.newsapp.util.Constants.DEFAULT_LOADING_DURATION_IN_MILLIS
import com.example.newsapp.util.Constants.PAGE_SIZE
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListScreenViewModel @Inject constructor(
    private val repository: NewsRepositoryImpl
) : ViewModel() {

    private var currentPage = 1

    var newsList = mutableStateOf<List<NewsEntity>>(listOf())
    var loadErrorMessage = mutableStateOf("")
    var isLoadingInitialNews = mutableStateOf(false)
    var paginationEndReached = mutableStateOf(false)
    var noOfflineNews = mutableStateOf(false)

    init {
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
            if (newsList.value.isEmpty()) {
                isLoadingInitialNews.value = true
            }
            val newsResponseWithTotalResults = repository.getNewsResponseWithTotalResults(page = currentPage)
            val newsEntitiesList = newsResponseWithTotalResults.data?.first
            val totalResults = newsResponseWithTotalResults.data?.second

            when (newsResponseWithTotalResults) {
                is Resource.Success -> {
                    paginationEndReached.value = currentPage * PAGE_SIZE >= (totalResults ?: PAGE_SIZE)
                    currentPage++
                    loadErrorMessage.value = ""
                    if (isLoadingInitialNews.value) {
                        if (newsEntitiesList != null) {
                            println("NewsDB: deleted")
                            repository.deleteDb()
                        }
                        isLoadingInitialNews.value = false
                    }
                    if (newsEntitiesList != null) {
                        repository.insertNewsEntitiesToDb(newsEntitiesList)
                        newsList.value += newsEntitiesList
                    }
                }

                is Resource.Error -> {
                    // If the news cannot be loaded, give a little time to the user to see the loading bar before the error message is displayed
                    delay(DEFAULT_LOADING_DURATION_IN_MILLIS)
                    loadErrorMessage.value = newsResponseWithTotalResults.message ?: "An unexpected error happened"
                    isLoadingInitialNews.value = false
                }
            }
        }
    }
}