package com.gsm.newscircle.utils

import com.gsm.newscircle.utils.logger.LoggerService

class TestLoggerService: LoggerService {
    override fun d(tag: String, message: String) {
        println("DEBUG: $tag: $message")
    }

    override fun e(tag: String, message: String) {
        println("ERROR: $tag: $message")
    }
}