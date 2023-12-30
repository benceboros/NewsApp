package com.example.newsapp.data.local.repositories

import com.example.newsapp.data.local.daos.NewsDao
import com.example.newsapp.data.local.entities.NewsEntity
import com.example.newsapp.data.remote.NetworkManager
import com.example.newsapp.data.remote.respones.Article
import com.example.newsapp.model.NewsItem
import com.example.newsapp.util.Resource
import com.example.newsapp.util.canBeSaved
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val newsDao: NewsDao
) : NewsRepository {

    override val updateNewsList: SharedFlow<List<NewsItem>>
        get() = TODO("Not yet implemented")

    override fun convertArticlesToNewsEntities(articles: List<Article>?): List<NewsEntity> {
        return articles?.mapNotNull { article ->
            if (article.canBeSaved()) {
                NewsEntity(
                    imageUrl = article.urlToImage,
                    title = article.title,
                    description = article.description,
                    publishDate = article.publishedAt,
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
            println("API ERROR: $e")
            return Resource.Error("Failed to get the top headlines based on the given country.")
        }
        val newsEntitiesList = convertArticlesToNewsEntities(response.articles)
        println("NewsDB: ${newsDao.getNews().size}")
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
}