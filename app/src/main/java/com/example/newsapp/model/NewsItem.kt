package com.example.newsapp.model

data class NewsItem(
    val imageUrl: String?,
    val title: String?,
    val description: String?,
    val publishDate: String?,
    val author: String?,
    val urlToArticle: String?
)
