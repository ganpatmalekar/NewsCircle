package com.gsm.newscircle.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.NewsListRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.AppConstants.NEWS_LIST_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val newsListRepository: NewsListRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _newsUiState: MutableStateFlow<UiState<List<ApiArticle>>> =
        MutableStateFlow(UiState.Loading)
    val newsUiState: StateFlow<UiState<List<ApiArticle>>> = _newsUiState

    private fun isInternetAvailable() : Boolean = networkHelper.isNetworkConnected()

    fun fetchAllNewsBySources(sources: String) {
        if (isInternetAvailable()) {
            getAllNewsBySources(sources)
        } else {
            _newsUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    private fun getAllNewsBySources(sources: String) {
        _newsUiState.value = UiState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            newsListRepository.getAllNewsBySource(sources)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsUiState.value = UiState.Error(handleError(e))
                    logger.e(NEWS_LIST_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _newsUiState.value = UiState.Success(it)
                    logger.d(NEWS_LIST_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }

    fun fetchAllNewsByCountry(country: String) {
        if (isInternetAvailable()) {
            getAllNewsByCountry(country)
        } else {
            _newsUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    private fun getAllNewsByCountry(country: String) {
        _newsUiState.value = UiState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            newsListRepository.getAllNewsByCountry(country)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsUiState.value = UiState.Error(handleError(e))
                    logger.e(NEWS_LIST_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _newsUiState.value = UiState.Success(it)
                    logger.d(NEWS_LIST_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }

    fun fetchAllNewsByLanguage(language: String) {
        if (isInternetAvailable()) {
            getAllNewsByLanguage(language)
        } else {
            _newsUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    private fun getAllNewsByLanguage(language: String) {
        _newsUiState.value = UiState.Loading
        viewModelScope.launch(dispatcherProvider.main) {
            newsListRepository.getAllNewsByLanguage(language)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsUiState.value = UiState.Error(handleError(e))
                    logger.e(NEWS_LIST_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _newsUiState.value = UiState.Success(it)
                    logger.d(NEWS_LIST_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }
}