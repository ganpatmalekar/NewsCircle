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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchNewsViewModel(
    private val searchNewsRepository: SearchNewsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper,
    private val logger: LoggerService
) : ViewModel() {
    private val _searchUiState = MutableStateFlow<UiState<List<ApiArticle>>>(UiState.Loading)
    val searchUiState: StateFlow<UiState<List<ApiArticle>>> = _searchUiState

    private var _queryStateFlow = MutableStateFlow("")
    private var _sortByStateFlow = MutableStateFlow(AppConstants.SORT_BY_OPTION)

    private fun isInternetAvailable(): Boolean = networkHelper.isNetworkConnected()

    init {
        if (isInternetAvailable()) {
            handleSearch()
        } else {
            _searchUiState.value = UiState.Error(AppConstants.NO_INTERNET_MSG)
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun handleSearch() {
        viewModelScope.launch(dispatcherProvider.main) {
            _queryStateFlow
                .combine(_sortByStateFlow) { query, sortBy ->
                    Pair(query, sortBy)
                }
                .debounce(AppConstants.DEBOUNCE_TIME)
                .distinctUntilChanged()
                .flatMapLatest { (query, sortBy) ->
                    _searchUiState.value = UiState.Loading
                    // If search query is empty then it will initialize with default query
                    val searchQuery = query.ifEmpty { AppConstants.DEFAULT_QUERY }
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
        _queryStateFlow.value = queryString
    }

    fun setSortByOption(sortBy: String) {
        _sortByStateFlow.value = sortBy
    }

    fun getSortOptions(): List<SortOption> {
        return Helper.getSortOptions().map {
            it.copy(isSelected = it.key == _sortByStateFlow.value)
        }
    }
}