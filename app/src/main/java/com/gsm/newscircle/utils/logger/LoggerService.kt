package com.gsm.newscircle.utils.logger

interface LoggerService {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
}