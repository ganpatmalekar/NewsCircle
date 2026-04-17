package com.gsm.newscircle.data.model.newssource

data class NewsSourcesResponse(
    val sources: List<ApiNewsSource>,
    val status: String
)