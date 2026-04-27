package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.model.Country
import com.gsm.newscircle.utils.Helper
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CountryListRepository @Inject constructor() {
    fun getCountries(): Flow<List<Country>> {
        return flow {
            emit(Helper.getSupportedNewsCountries())
        }
    }
}