package com.gsm.newscircle.data.local

import com.gsm.newscircle.data.local.entity.Article
import kotlinx.coroutines.flow.Flow

class DatabaseServiceImpl(private val database: NewsCircleDatabase) : DatabaseService {
    override fun getAllTopHeadlines(countryCode: String): Flow<List<Article>> {
        return database.topHeadlinesDao().getAllTopHeadlines(countryCode)
    }

    override fun deleteAndInsertAllTopHeadlines(
        articles: List<Article>,
        countryCode: String
    ) {
        database.topHeadlinesDao().deleteAndInsertAllTopHeadlines(articles, countryCode)
    }

    override fun getArticlesByLanguage(languageID: String): Flow<List<Article>> {
        return database.topHeadlinesDao().getAllArticlesByLanguage(languageID)
    }

    override fun deleteAndInsertAllArticlesByLanguage(
        articles: List<Article>,
        languageID: String
    ) {
        database.topHeadlinesDao().deleteAndInsertAllArticlesByLanguage(articles, languageID)
    }
}