package com.gsm.newscircle.data.local

import com.gsm.newscircle.data.local.entity.Article
import kotlinx.coroutines.flow.Flow

interface DatabaseService {
    fun getAllTopHeadlines(countryCode: String): Flow<List<Article>>

    fun deleteAndInsertAllTopHeadlines(articles: List<Article>, countryCode: String)

    fun getArticlesByLanguage(languageID: String): Flow<List<Article>>

    fun deleteAndInsertAllArticlesByLanguage(articles: List<Article>, languageID: String)
}