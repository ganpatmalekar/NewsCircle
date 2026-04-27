package com.gsm.newscircle.data.repository

import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.local.DatabaseService
import com.gsm.newscircle.data.local.entity.Article
import com.gsm.newscircle.data.model.topheadline.toArticleEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class TopHeadlineOfflineRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) {
    fun getAllTopHeadlines(countryCode: String): Flow<List<Article>> {
        return flow {
            val response = networkService.getTopHeadlines(country = countryCode)
            val articles = response.apiArticles.map { apiArticle ->
                apiArticle.toArticleEntity(countryCode)
            }
            // Data is saved to DB for offline support
            deleteAndInsertAllTopHeadlines(articles, countryCode)
            emit(articles)
        }
    }

    fun getAllTopHeadlinesFromDB(country: String): Flow<List<Article>> {
        return databaseService.getAllTopHeadlines(country)
    }

    fun deleteAndInsertAllTopHeadlines(articles: List<Article>, country: String) {
        databaseService.deleteAndInsertAllTopHeadlines(articles, country)
    }
}