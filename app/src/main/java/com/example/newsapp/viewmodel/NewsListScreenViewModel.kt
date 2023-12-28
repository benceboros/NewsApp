package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsListScreenViewModel @Inject constructor(

) : ViewModel() {
    val newsItem = News(
        imageUrl = "https://cdn.arstechnica.net/wp-content/uploads/2023/09/Screenshot-2023-09-12-at-12.11.31-PM-1-760x380.jpg",
        title = "Appeals court pauses ban on patent-infringing Apple Watch imports - Ars Technica",
        description = "Enlarge/ The Apple Watch Series 9 released in September 2023. \r\n59\r\nJust before Christmas, Apple pulled two of its latest smartwatches from stores. The cause was not an unwelcome visit from the ghost… [+2291 chars]",
        publishDate = "2023-12-27T17:27:26Z",
        author = "Jonathan M. Gitlin",
        urlToArticle = "https://arstechnica.com/gadgets/2023/12/apple-appeals-trade-commission-ban-of-apple-watch-9-apple-watch-ultra-2/"
    )
    val newsList: List<News> = List(30) { newsItem }
}