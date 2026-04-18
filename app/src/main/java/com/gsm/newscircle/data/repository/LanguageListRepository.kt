package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.model.Language
import com.gsm.newscircle.utils.Helper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageListRepository @Inject constructor() {
    fun getAllSupportedLanguages(): Flow<List<Language>> {
        return flow {
            emit(Helper.getSupportedNewsLanguages())
        }
    }
}