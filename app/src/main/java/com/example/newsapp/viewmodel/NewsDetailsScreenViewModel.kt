package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapp.model.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsDetailsScreenViewModel @Inject constructor(

) : ViewModel() {
    val selectedNewsItem = NewsItem(
        imageUrl = "https://cdn.arstechnica.net/wp-content/uploads/2023/09/Screenshot-2023-09-12-at-12.11.31-PM-1-760x380.jpg",
        title = "Appeals court pauses ban on patent-infringing Apple Watch imports - Ars Technica",
        description = "Apple pulled the Watch Series 9 and Watch Ultra 2 from sale on December 21.",
        publishDate = "2023-12-27T17:27:26Z",
        author = "Jonathan M. Gitlin",
        urlToArticle = "https://arstechnica.com/gadgets/2023/12/apple-appeals-trade-commission-ban-of-apple-watch-9-apple-watch-ultra-2/"
    )
}