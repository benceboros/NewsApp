package com.example.newsapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.model.NewsItem

@Entity
data class NewsEntity (
    @PrimaryKey val id: Int? = null,
    val imageUrl: String?,
    val title: String?,
    val description: String?,
    val publishDate: String?,
    val author: String?,
    val urlToArticle: String?
) {
    fun toNewsItem(): NewsItem {
        return NewsItem(
            imageUrl = imageUrl,
            title = title,
            description = description,
            publishDate = publishDate,
            author = author,
            urlToArticle = urlToArticle
        )
    }
}