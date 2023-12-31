package com.example.newsapp.data.remote.respones

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsList(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?
)