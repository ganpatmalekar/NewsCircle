package com.gsm.newscircle.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.utils.AppConstants.PAGE_SIZE
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TopHeadlinePagingRepository @Inject constructor(private val networkService: NetworkService) {
    fun getTopHeadlines(): Flow<PagingData<ApiArticle>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { TopHeadlinePagingSource(networkService) }
        ).flow
    }
}