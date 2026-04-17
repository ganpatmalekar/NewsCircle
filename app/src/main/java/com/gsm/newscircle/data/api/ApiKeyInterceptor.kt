package com.gsm.newscircle.data.api

import com.gsm.newscircle.di.NetworkApiKey
import com.gsm.newscircle.utils.AppConstants
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(@param:NetworkApiKey private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest
            .newBuilder()
            .header(AppConstants.API_KEY_HEADER, apiKey)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}