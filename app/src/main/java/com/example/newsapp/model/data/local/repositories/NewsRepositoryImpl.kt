package com.example.newsapp.model.data.local.repositories

import android.util.Log
import com.example.newsapp.model.data.local.daos.NewsDao
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.model.data.remote.NetworkManager
import com.example.newsapp.model.data.remote.respones.Article
import com.example.newsapp.util.Resource
import com.example.newsapp.util.canBeSaved
import com.example.newsapp.util.formatToAppropriateDateFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val newsDao: NewsDao
) : NewsRepository {

    private fun convertArticlesToNewsEntities(articles: List<Article>?): List<NewsEntity> {
        return articles?.mapNotNull { article ->
            if (article.canBeSaved()) {
                NewsEntity(
                    imageUrl = article.urlToImage,
                    title = article.title,
                    description = article.description,
                    publishDate = article.publishedAt?.formatToAppropriateDateFormat(),
                    author = article.author,
                    urlToArticle = article.url
                )
            } else null
        } ?: emptyList()
    }

    override suspend fun getNewsResponseWithTotalResults(page: Int): Resource<Pair<List<NewsEntity>, Int?>> {
        val response = try {
            networkManager.getTopHeadlinesBasedOnCountry(page = page)
        } catch (e: Exception) {
            Log.e("API Error", "Reason: $e")
            return Resource.Error("Failed to get the top headlines based on the given country.")
        }
        val newsEntitiesList = convertArticlesToNewsEntities(response.articles)
        return Resource.Success(newsEntitiesList to response.totalResults)
    }

    override suspend fun insertNewsEntitiesToDb(newsEntitiesList: List<NewsEntity>) {
        newsDao.insertNewsEntities(newsEntitiesList)
    }

    override suspend fun getDbSavedNewsList(): List<NewsEntity> {
        return newsDao.getNews()
    }

    override suspend fun deleteDb() {
        newsDao.deleteNewsEntities()
    }

    override suspend fun getSelectedNewsEntity(id: Int): NewsEntity? {
        return newsDao.getSelectedNewsEntity(id)
    }
}