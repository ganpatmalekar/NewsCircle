package com.gsm.newscircle.ui.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.TopHeadlinePagingRepository
import kotlinx.coroutines.flow.Flow

class TopHeadlinePaginationViewModel(topHeadlinePagingRepository: TopHeadlinePagingRepository) :
    ViewModel() {

    val topHeadlineUiState: Flow<PagingData<ApiArticle>> =
        topHeadlinePagingRepository.getTopHeadlines()
            .cachedIn(viewModelScope)

}