package com.gsm.newscircle.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TopHeadlinesArticle")
data class Article(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "articleId")
    val id: Int = 0,
    val author: String? = "",
    val content: String? = null,
    val description: String? = "",
    val publishedAt: String,
    @Embedded
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    val country: String? = null,
    val language: String? = null
)