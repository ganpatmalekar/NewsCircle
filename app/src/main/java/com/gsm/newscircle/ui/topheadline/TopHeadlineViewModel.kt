package com.gsm.newscircle.ui.topheadline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.AppConstants.TOP_HEADLINE_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TopHeadlineViewModel(
    private val repository: TopHeadlineRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _topHeadlineUiState: MutableStateFlow<UiState<List<ApiArticle>>> =
        MutableStateFlow(UiState.Loading)
    val topHeadlineUiState: StateFlow<UiState<List<ApiArticle>>> = _topHeadlineUiState

    init {
        fetchTopHeadlines()
    }

    private fun isInternetAvailable(): Boolean = networkHelper.isNetworkConnected()

    fun fetchTopHeadlines() {
        if (isInternetAvailable()) {
            getAllTopHeadlines()
        } else {
            _topHeadlineUiState.value =
                UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    private fun getAllTopHeadlines() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getTopHeadlines(AppConstants.COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlineUiState.value = UiState.Error(Helper.handleError(e))
                    logger.e(TOP_HEADLINE_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _topHeadlineUiState.value = UiState.Success(it)
                    logger.d(TOP_HEADLINE_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }
}