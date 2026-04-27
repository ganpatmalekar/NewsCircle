package com.gsm.newscircle.ui.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.Country
import com.gsm.newscircle.data.repository.CountryListRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants.COUNTRY_LIST_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val countryListRepository: CountryListRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: LoggerService
) : ViewModel() {
    private val _countryUiState = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)
    val countryUiState: StateFlow<UiState<List<Country>>> = _countryUiState

    private val _selectedCountry = MutableStateFlow(Country("United States", "us", "US"))
    val selectedCountry: StateFlow<Country> get() = _selectedCountry

    fun setCountry(country: Country) {
        _selectedCountry.value = country
        logger.d(COUNTRY_LIST_VIEWMODEL_TAG, country.name)
    }

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            countryListRepository.getCountries()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _countryUiState.value = UiState.Error(handleError(e))
                    logger.e(COUNTRY_LIST_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _countryUiState.value = UiState.Success(it)
                }
        }
    }

}