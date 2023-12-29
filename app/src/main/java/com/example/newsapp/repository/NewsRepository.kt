package com.example.newsapp.repository

import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.remote.respones.NewsList
import com.example.newsapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NewsRepository @Inject constructor(
    private val api: NewsApi
){
    suspend fun getTopHeadlinesBasedOnCountry(
        country: String,
        apiKey: String,
        pageSize: Int,
        page: Int,
    ): Resource<NewsList> {
        val response = try {
            api.getTopHeadlinesBasedOnCountry(
                country = country,
                apiKey = apiKey,
                pageSize = pageSize,
                page = page
            )
        } catch(e: Exception) {
            return Resource.Error("Failed to get the top headlines based on the given country.")
        }
        return Resource.Success(response)
    }
}