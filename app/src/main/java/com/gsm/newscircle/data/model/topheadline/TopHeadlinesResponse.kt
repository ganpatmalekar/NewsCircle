package com.gsm.newscircle.data.model.topheadline

import com.google.gson.annotations.SerializedName

data class TopHeadlinesResponse(
    @SerializedName("articles")
    val apiArticles: List<ApiArticle>,
    val status: String,
    val totalResults: Int
)