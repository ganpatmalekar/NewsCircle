package com.gsm.newscircle.data.model

data class SortOption(
    val title: String,
    val desc: String,
    val key: String,
    var isSelected: Boolean = false
)
