package com.gsm.newscircle.utils

import java.util.Calendar

object TimeUtil {
    fun getInitialDelay(): Long {
        val currentTime = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, AppConstants.NEWS_UPDATE_TIME)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (dueDate.before(currentTime)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val initialDelay = dueDate.timeInMillis - currentTime.timeInMillis
        return initialDelay
    }
}