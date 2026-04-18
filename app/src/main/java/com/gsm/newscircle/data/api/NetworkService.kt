package com.gsm.newscircle.data.api

import com.gsm.newscircle.data.model.newssource.NewsSourcesResponse
import com.gsm.newscircle.data.model.topheadline.TopHeadlinesResponse
import com.gsm.newscircle.utils.AppConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): TopHeadlinesResponse

    @GET("top-headlines/sources")
    suspend fun getNewsSources(): NewsSourcesResponse

    @GET("top-headlines")
    suspend fun getNewsBySource(
        @Query("sources") sources: String
    ): TopHeadlinesResponse

    @GET("top-headlines")
    suspend fun getNewsByCountry(
        @Query("country") country: String
    ): TopHeadlinesResponse

    @GET("everything")
    suspend fun getNewsByLanguage(
        @Query("q") query: String = AppConstants.DEFAULT_QUERY,
        @Query("language") language: String
    ): TopHeadlinesResponse
}