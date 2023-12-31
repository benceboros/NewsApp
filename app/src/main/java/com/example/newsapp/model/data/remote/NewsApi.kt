package com.example.newsapp.model.data.remote

import com.example.newsapp.model.data.remote.respones.NewsList
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlinesBasedOnCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NewsList
}