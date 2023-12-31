package com.example.newsapp.util

import com.example.newsapp.data.remote.respones.Article
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Article.canBeSaved(): Boolean =
    urlToImage != null && title != null && publishedAt != null

fun String.formatToAppropriateDateFormat(): String {
    val date = ZonedDateTime.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm")
    return date.format(formatter)
}