package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class SearchNewsRepository @Inject constructor(private val networkService: NetworkService) {
    fun getAllNewsByQuery(queryString: String, sortBy: String): Flow<List<ApiArticle>> {
        return flow {
            emit(networkService.getNewsByQuery(query = queryString, sortBy = sortBy))
        }.map {
            it.apiArticles
        }
    }
}