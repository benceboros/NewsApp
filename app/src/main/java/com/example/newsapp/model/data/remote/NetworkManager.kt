package com.example.newsapp.model.data.remote

import com.example.newsapp.model.data.remote.respones.NewsList
import com.example.newsapp.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    private val api: NewsApi
) {
    suspend fun getTopHeadlinesBasedOnCountry(
        page: Int
    ): NewsList {
        return api.getTopHeadlinesBasedOnCountry(
            country = Constants.COUNTRY_CODE,
            apiKey = Constants.API_KEY,
            pageSize = Constants.PAGE_SIZE,
            page = page
        )
    }
}