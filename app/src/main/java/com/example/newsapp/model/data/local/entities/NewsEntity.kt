package com.example.newsapp.model.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val imageUrl: String?,
    val title: String?,
    val description: String?,
    val publishDate: String?,
    val author: String?,
    val urlToArticle: String?
)