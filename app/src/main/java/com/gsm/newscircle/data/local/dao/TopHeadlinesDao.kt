package com.gsm.newscircle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gsm.newscircle.data.local.entity.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface TopHeadlinesDao {
    @Query("SELECT * FROM TopHeadlinesArticle WHERE country = :country")
    fun getAllTopHeadlines(country: String): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTopHeadlines(articles: List<Article>)

    @Query("DELETE FROM TopHeadlinesArticle WHERE country = :country")
    fun clearAllTopHeadlines(country: String)

    @Transaction
    fun deleteAndInsertAllTopHeadlines(articles: List<Article>, country: String) {
        clearAllTopHeadlines(country)
        insertAllTopHeadlines(articles)
    }

    @Query("SELECT * FROM TopHeadlinesArticle WHERE language =:languageCode")
    fun getAllArticlesByLanguage(languageCode: String): Flow<List<Article>>

    @Query("DELETE FROM TopHeadlinesArticle WHERE language =:languageCode")
    fun clearAllArticlesByLanguage(languageCode: String)

    @Transaction
    fun deleteAndInsertAllArticlesByLanguage(articles: List<Article>, languageCode: String) {
        clearAllArticlesByLanguage(languageCode)
        insertAllTopHeadlines(articles)
    }
}