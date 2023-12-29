package com.example.newsapp.data.remote.respones

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    val id: String?,
    val name: String?
)