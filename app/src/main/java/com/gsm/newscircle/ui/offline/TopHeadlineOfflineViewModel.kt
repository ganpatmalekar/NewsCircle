package com.gsm.newscircle.ui.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.local.entity.Article
import com.gsm.newscircle.data.repository.TopHeadlineOfflineRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TopHeadlineOfflineViewModel(
    private val topHeadlineOfflineRepository: TopHeadlineOfflineRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _topHeadlineOfflineUiState =
        MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val topHeadlineOfflineUiState: StateFlow<UiState<List<Article>>> = _topHeadlineOfflineUiState

    init {
        fetchAllTopHeadlines()
    }

    private fun isInternetAvailable(): Boolean = networkHelper.isNetworkConnected()

    fun fetchAllTopHeadlines() {
        fetchTopHeadlinesFromDB()
        if (isInternetAvailable()) {
            fetchTopHeadlinesFromNetwork()
        }
    }

    private fun fetchTopHeadlinesFromNetwork() {
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlineOfflineRepository.getAllTopHeadlines(AppConstants.COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    logger.e(AppConstants.TOP_HEADLINE_OFFLINE_VIEWMODEL_TAG, e.toString())
                    // We don't set UiState.Error here if we already have DB data
                    if (_topHeadlineOfflineUiState.value !is UiState.Success) {
                        _topHeadlineOfflineUiState.value = UiState.Error(handleError(e))
                    }
                }
                .collect {
                    // If network returns nothing and DB is already empty,
                    // we might want to ensure the Loading state is replaced with an error/empty state.
                    if (_topHeadlineOfflineUiState.value is UiState.Loading) {
                        _topHeadlineOfflineUiState.value = UiState.Error("No articles found")
                    }
                    logger.d(
                        AppConstants.TOP_HEADLINE_OFFLINE_VIEWMODEL_TAG,
                        "Network sync completed successfully"
                    )
                }
        }
    }

    private fun fetchTopHeadlinesFromDB() {
        viewModelScope.launch(dispatcherProvider.main) {
            topHeadlineOfflineRepository.getAllTopHeadlinesFromDB(AppConstants.COUNTRY)
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _topHeadlineOfflineUiState.value = UiState.Error(handleError(e))
                }
                .collect {
                    if (it.isNotEmpty()) {
                        _topHeadlineOfflineUiState.value = UiState.Success(it)
                    } else if (!isInternetAvailable()) {
                        _topHeadlineOfflineUiState.value =
                            UiState.Error(AppConstants.NO_INTERNET_MSG)
                    }
                }
        }
    }


}