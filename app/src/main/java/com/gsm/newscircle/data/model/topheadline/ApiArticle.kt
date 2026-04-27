package com.gsm.newscircle.data.model.topheadline

import com.google.gson.annotations.SerializedName
import com.gsm.newscircle.data.local.entity.Article

data class ApiArticle(
    val author: String? = "",
    val content: String,
    val description: String? = "",
    val publishedAt: String,
    @SerializedName("source")
    val apiSource: ApiSource,
    val title: String,
    val url: String,
    val urlToImage: String
)

fun ApiArticle.toArticleEntity(country: String): Article {
    return Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = apiSource.toSourceEntity(),
        title = title,
        url = url,
        urlToImage = urlToImage,
        country = country
    )
}

fun List<ApiArticle>.apiArticleListToArticleList(country: String) : List<Article> {
    val list = mutableListOf<Article>()
    forEach { apiArticle ->
        list.add(apiArticle.toArticleEntity(country))
    }
    return list
}

fun ApiArticle.toArticleByLanguage(language: String): Article {
    return Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = apiSource.toSourceEntity(),
        title = title,
        url = url,
        urlToImage = urlToImage,
        language = language
    )
}