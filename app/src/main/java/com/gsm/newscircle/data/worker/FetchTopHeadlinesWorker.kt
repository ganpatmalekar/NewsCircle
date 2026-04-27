package com.gsm.newscircle.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.local.DatabaseService
import com.gsm.newscircle.data.model.topheadline.apiArticleListToArticleList
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.NotificationUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchTopHeadlinesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val networkService: NetworkService,
    private val databaseService: DatabaseService
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Fetch from network
            val response = networkService.getTopHeadlines(AppConstants.COUNTRY)

            // 2. Save to database
            val articles = response.apiArticles
                .apiArticleListToArticleList(AppConstants.COUNTRY)
            databaseService.deleteAndInsertAllTopHeadlines(articles, AppConstants.COUNTRY)

            // 3. Show Notification
            NotificationUtil.showNotification(
                applicationContext,
                title = AppConstants.NOTIFICATION_CONTENT_TITLE,
                message = AppConstants.NOTIFICATION_CONTENT_TEXT
            )

            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
