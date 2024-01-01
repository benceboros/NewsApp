package com.example.newsapp.model.data.local.repositories

import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.model.data.remote.respones.Article
import com.example.newsapp.util.Resource

interface NewsRepository {
    suspend fun getNewsResponseWithTotalResults(page: Int): Resource<Pair<List<NewsEntity>, Int?>>

    suspend fun insertNewsEntitiesToDb(newsEntitiesList: List<NewsEntity>)

    suspend fun getDbSavedNewsList(): List<NewsEntity>

    suspend fun deleteDb()

    suspend fun getSelectedNewsEntity(id: Int): NewsEntity?
}