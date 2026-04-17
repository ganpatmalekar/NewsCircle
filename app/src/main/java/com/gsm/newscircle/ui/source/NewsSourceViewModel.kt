package com.gsm.newscircle.ui.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.newssource.ApiNewsSource
import com.gsm.newscircle.data.repository.NewsSourceRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.AppConstants.NEWS_SOURCE_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class NewsSourceViewModel(
    private val newsSourceRepository: NewsSourceRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _newsSourceUiState: MutableStateFlow<UiState<List<ApiNewsSource>>> =
        MutableStateFlow(UiState.Loading)
    val newsSourceUiState: StateFlow<UiState<List<ApiNewsSource>>> = _newsSourceUiState

    init {
        fetchAllNewsSources()
    }

    private fun isInternetAvailable() = networkHelper.isNetworkConnected()

    fun fetchAllNewsSources() {
        if (isInternetAvailable()) {
            getAllNewsSources()
        } else {
            _newsSourceUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    private fun getAllNewsSources() {
        viewModelScope.launch(dispatcherProvider.main) {
            newsSourceRepository.getAllNewsSources()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _newsSourceUiState.value = UiState.Error(handleError(e))
                    logger.e(NEWS_SOURCE_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _newsSourceUiState.value = UiState.Success(it)
                    logger.d(NEWS_SOURCE_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }
}