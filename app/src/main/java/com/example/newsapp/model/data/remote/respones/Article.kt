package com.example.newsapp.model.data.remote.respones

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)