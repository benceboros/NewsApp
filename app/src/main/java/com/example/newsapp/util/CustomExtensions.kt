package com.example.newsapp.util

import com.example.newsapp.data.remote.respones.Article

fun Article.canBeSaved() : Boolean =
    urlToImage != null && title != null && publishedAt != null
