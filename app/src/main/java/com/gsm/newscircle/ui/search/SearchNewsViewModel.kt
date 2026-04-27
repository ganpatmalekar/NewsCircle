package com.gsm.newscircle.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.SortOption
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.SearchNewsRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.AppConstants.SEARCH_NEWS_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val searchNewsRepository: SearchNewsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _searchUiState = MutableStateFlow<UiState<List<ApiArticle>>>(UiState.Loading)
    val searchUiState: StateFlow<UiState<List<ApiArticle>>> = _searchUiState

    private var _queryStateFlow = MutableStateFlow("")
    private var _sortByStateFlow = MutableStateFlow(AppConstants.SORT_BY_OPTION)
    private val _searchTrigger = MutableSharedFlow<Unit>(replay = 1)

    private fun isInternetAvailable(): Boolean = networkHelper.isNetworkConnected()

    init {
        _searchTrigger.tryEmit(Unit)
        handleSearch()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handleSearch() {
        viewModelScope.launch(dispatcherProvider.main) {
            _searchTrigger
                .flatMapLatest {
                    val query = _queryStateFlow.value
                    val sortBy = _sortByStateFlow.value

                    if (!isInternetAvailable()) {
                        _searchUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
                        return@flatMapLatest emptyFlow()
                    }
                    val searchQuery = when {
                        query.isEmpty() -> AppConstants.DEFAULT_QUERY
                        query.isBlank() -> {
                            _searchUiState.value = UiState.Error(AppConstants.BLANK_QUERY_MSG)
                            return@flatMapLatest emptyFlow<List<ApiArticle>>()
                        }
                        else -> query.trim()
                    }
                    _searchUiState.value = UiState.Loading
                    searchNewsRepository.getAllNewsByQuery(searchQuery, sortBy)
                        .catch { e ->
                            _searchUiState.value = UiState.Error(handleError(e))
                            logger.e(SEARCH_NEWS_VIEWMODEL_TAG, e.toString())
                        }
                }
                .flowOn(dispatcherProvider.io)
                .collect {
                    _searchUiState.value = UiState.Success(it)
                    logger.d(SEARCH_NEWS_VIEWMODEL_TAG, it.size.toString())
                }
        }
    }

    fun searchNewsByQuery(queryString: String) {
        if (_queryStateFlow.value != queryString) {
            _queryStateFlow.value = queryString
            _searchTrigger.tryEmit(Unit)
        }
    }

    fun setSortByOption(sortBy: String) {
        if (_sortByStateFlow.value != sortBy) {
            _sortByStateFlow.value = sortBy
            _searchTrigger.tryEmit(Unit)
        }
    }

    fun retry() {
        _searchTrigger.tryEmit(Unit)
    }

    fun getSortOptions(): List<SortOption> {
        return Helper.getSortOptions().map {
            it.copy(isSelected = it.key == _sortByStateFlow.value)
        }
    }
}