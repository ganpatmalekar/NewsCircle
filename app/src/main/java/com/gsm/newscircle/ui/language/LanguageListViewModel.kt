package com.gsm.newscircle.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsm.newscircle.data.model.Language
import com.gsm.newscircle.data.repository.LanguageListRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants.COUNTRY_LIST_VIEWMODEL_TAG
import com.gsm.newscircle.utils.AppConstants.LANGUAGE_LIST_VIEWMODEL_TAG
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LanguageListViewModel(
    private val languageListRepository: LanguageListRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val logger: LoggerService
) : ViewModel() {
    private val _languageUiState = MutableStateFlow<UiState<List<Language>>>(UiState.Loading)
    val languageUiState: StateFlow<UiState<List<Language>>> = _languageUiState

    private val _selectedLanguage = MutableStateFlow<Language>(Language("English", "en"))
    val selectedLanguage: StateFlow<Language> = _selectedLanguage

    fun setLanguage(language: Language) {
        _selectedLanguage.value = language
        logger.d(LANGUAGE_LIST_VIEWMODEL_TAG, language.name)
    }

    init {
        fetchLanguages()
    }

    private fun fetchLanguages() {
        viewModelScope.launch {
            languageListRepository.getAllSupportedLanguages()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _languageUiState.value = UiState.Error(handleError(e))
                    logger.e(COUNTRY_LIST_VIEWMODEL_TAG, e.toString())
                }
                .collect {
                    _languageUiState.value = UiState.Success(it)
                }
        }
    }
}