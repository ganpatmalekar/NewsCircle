package com.gsm.newscircle.data.local.entity

import androidx.room.ColumnInfo

data class Source(
    @ColumnInfo("sourceId")
    val id: String? = null,
    val name: String
)
