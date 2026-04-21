package com.gsm.newscircle.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sources")
data class NewsSource(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "sourceId")
    val sourceId: String,
    val category: String,
    val country: String,
    val description: String,
    val language: String,
    val name: String,
    val url: String
)
