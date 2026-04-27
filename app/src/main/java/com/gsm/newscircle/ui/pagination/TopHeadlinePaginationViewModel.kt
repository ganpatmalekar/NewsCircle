package com.gsm.newscircle.ui.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.TopHeadlinePagingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TopHeadlinePaginationViewModel @Inject constructor(
    topHeadlinePagingRepository: TopHeadlinePagingRepository
) : ViewModel() {

    val topHeadlineUiState: Flow<PagingData<ApiArticle>> =
        topHeadlinePagingRepository.getTopHeadlines()
            .cachedIn(viewModelScope)

}