package com.example.newsapp.data.local.repositories

import com.example.newsapp.data.local.entities.NewsEntity
import com.example.newsapp.data.remote.respones.Article
import com.example.newsapp.data.remote.respones.NewsList
import com.example.newsapp.model.NewsItem
import com.example.newsapp.util.Resource
import kotlinx.coroutines.flow.SharedFlow

interface NewsRepository {
    val updateNewsList: SharedFlow<List<NewsItem>>

    fun convertArticlesToNewsEntities(articles: List<Article>?): List<NewsEntity>

    suspend fun getNewsResponseWithTotalResults(page: Int): Resource<Pair<List<NewsEntity>, Int?>>

    suspend fun insertNewsEntitiesToDb(newsEntitiesList: List<NewsEntity>)

    suspend fun getDbSavedNewsList(): List<NewsEntity>

    suspend fun deleteDb()

    suspend fun getSelectedNewsEntity(id: Int): NewsEntity
}