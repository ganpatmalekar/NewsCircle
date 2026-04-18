package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsListRepository @Inject constructor(private val networkService: NetworkService) {
    fun getAllNewsBySource(sources: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getNewsBySource(sources))
        }.map {
            it.apiArticles
        }
    }

    fun getAllNewsByCountry(country: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getNewsByCountry(country))
        }.map {
            it.apiArticles
        }
    }
}