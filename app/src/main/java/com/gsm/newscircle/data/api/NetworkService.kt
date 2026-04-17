package com.gsm.newscircle.data.api

import com.gsm.newscircle.data.model.topheadline.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String
    ): TopHeadlinesResponse
}