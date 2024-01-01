package com.example.newsapp.model.data.local.repositories

import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.util.Resource

class MockNewsRepository : NewsRepository {

    val newsList = mutableListOf<NewsEntity>()
    private var shouldReturnNetworkError = false

    fun shouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun populateNewsListWithMockData() {
        val testNewsEntity1 = NewsEntity(
            id = 1,
            imageUrl = "imageUrl1",
            title = "title1",
            description = "description1",
            publishDate = "publishDate1",
            author = "author1",
            urlToArticle = "url1"
        )

        val testNewsEntity2 = NewsEntity(
            id = 2,
            imageUrl = "imageUrl2",
            title = "title2",
            description = "description2",
            publishDate = "publishDate2",
            author = "author2",
            urlToArticle = "url2"
        )

        val testNewsEntity3 = NewsEntity(
            id = 3,
            imageUrl = "imageUrl3",
            title = "title3",
            description = "description3",
            publishDate = "publishDate3",
            author = "author3",
            urlToArticle = "url3"
        )
        newsList.addAll(listOf(testNewsEntity1, testNewsEntity2, testNewsEntity3))
    }

    override suspend fun getNewsResponseWithTotalResults(page: Int): Resource<Pair<List<NewsEntity>, Int?>> {
        return if (shouldReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            populateNewsListWithMockData()
            Resource.Success(newsList to newsList.size)
        }
    }

    override suspend fun insertNewsEntitiesToDb(newsEntitiesList: List<NewsEntity>) {
        newsList.addAll(newsEntitiesList)
    }

    override suspend fun getDbSavedNewsList(): List<NewsEntity> {
        return newsList
    }

    override suspend fun deleteDb() {
        newsList.removeAll(newsList)
    }

    override suspend fun getSelectedNewsEntity(id: Int): NewsEntity? {
        return newsList.find { it.id == id }
    }

}