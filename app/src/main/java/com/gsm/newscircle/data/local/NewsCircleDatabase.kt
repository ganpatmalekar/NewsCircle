package com.gsm.newscircle.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gsm.newscircle.data.local.dao.TopHeadlinesDao
import com.gsm.newscircle.data.local.entity.Article
import com.gsm.newscircle.data.local.entity.NewsSource

@Database(entities = [Article::class, NewsSource::class], version = 1, exportSchema = false)
abstract class NewsCircleDatabase : RoomDatabase() {
    abstract fun topHeadlinesDao(): TopHeadlinesDao
}