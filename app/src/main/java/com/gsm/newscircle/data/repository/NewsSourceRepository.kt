package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.newssource.ApiNewsSource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class NewsSourceRepository @Inject constructor(private val networkService: NetworkService) {
    fun getAllNewsSources(): Flow<List<ApiNewsSource>> {
        return flow {
            emit(networkService.getNewsSources())
        }.map {
            it.sources
        }
    }
}