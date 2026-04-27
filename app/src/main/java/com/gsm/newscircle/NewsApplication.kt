package com.gsm.newscircle

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gsm.newscircle.data.worker.FetchTopHeadlinesWorker
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.NotificationUtil
import com.gsm.newscircle.utils.TimeUtil
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        NotificationUtil.createNotificationChannel(this)
        setupDailyTopHeadlinesFetchWork()
    }

    private fun setupDailyTopHeadlinesFetchWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        // Create work request - Running every 24 hours with an initial delay to hit 6 AM
        val dailyWorkRequest =
            PeriodicWorkRequestBuilder<FetchTopHeadlinesWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(TimeUtil.getInitialDelay(), TimeUnit.MILLISECONDS)
                .addTag(AppConstants.UNIQUE_WORK_NAME)
                .build()

        // Submit work request
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            AppConstants.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }
}