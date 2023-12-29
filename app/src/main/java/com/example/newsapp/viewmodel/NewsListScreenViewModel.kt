package com.example.newsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.respones.Article
import com.example.newsapp.model.NewsItem
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Constants.API_KEY
import com.example.newsapp.util.Constants.COUNTRY_CODE
import com.example.newsapp.util.Constants.PAGE_SIZE
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListScreenViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var currentPage = 1

    var newsList = mutableStateOf<List<NewsItem>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var paginationEndReached = mutableStateOf(false)

    init {
        loadNewsListPaginated()
    }

    fun loadNewsListPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getTopHeadlinesBasedOnCountry(
                country = COUNTRY_CODE,
                apiKey = API_KEY,
                pageSize = PAGE_SIZE,
                page = currentPage
            )
            when(result) {
                is Resource.Success -> {
                    paginationEndReached.value = currentPage * PAGE_SIZE >= result.data?.totalResults!!
                    val newsItems: List<NewsItem>? = result.data.articles?.mapNotNull { article ->
                        if (article.canBeDisplayed()) {
                            NewsItem(
                                imageUrl = article.urlToImage,
                                title = article.title,
                                description = article.description,
                                publishDate = article.publishedAt,
                                author = article.author,
                                urlToArticle = article.url
                            )
                        } else null
                    }

                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    if (newsItems != null) {
                        newsList.value += newsItems
                    }
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    private fun Article.canBeDisplayed() : Boolean =
        urlToImage != null && title != null && publishedAt != null
}