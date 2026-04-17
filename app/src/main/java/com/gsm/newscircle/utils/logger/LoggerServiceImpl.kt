package com.gsm.newscircle.utils.logger

import android.util.Log

class LoggerServiceImpl: LoggerService {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}