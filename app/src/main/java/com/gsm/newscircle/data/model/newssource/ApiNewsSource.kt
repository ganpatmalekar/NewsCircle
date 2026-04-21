package com.gsm.newscircle.data.model.newssource

import com.gsm.newscircle.data.local.entity.NewsSource

data class ApiNewsSource(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)

fun ApiNewsSource.toNewsSourceEntity(): NewsSource {
    return NewsSource(
        category = category,
        country = country,
        description = description,
        sourceId = id,
        language = language,
        name = name,
        url = url
    )
}