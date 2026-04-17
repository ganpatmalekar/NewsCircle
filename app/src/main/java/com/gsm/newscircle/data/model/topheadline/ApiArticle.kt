package com.gsm.newscircle.data.model.topheadline

import com.google.gson.annotations.SerializedName

data class ApiArticle(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    @SerializedName("source")
    val apiSource: ApiSource,
    val title: String,
    val url: String,
    val urlToImage: String
)