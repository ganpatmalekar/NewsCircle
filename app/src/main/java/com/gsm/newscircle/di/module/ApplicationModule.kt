package com.gsm.newscircle.di.module

import android.content.Context
import com.gsm.newscircle.BuildConfig
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.data.api.ApiKeyInterceptor
import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.di.ApplicationContext
import com.gsm.newscircle.di.BaseUrl
import com.gsm.newscircle.di.NetworkApiKey
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.DefaultDispatcherProvider
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.NetworkHelperImpl
import com.gsm.newscircle.utils.logger.LoggerService
import com.gsm.newscircle.utils.logger.LoggerServiceImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: NewsApplication) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context = application

    @BaseUrl
    @Provides
    fun providesBaseUrl() = AppConstants.BASE_URL

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @NetworkApiKey
    @Provides
    fun providesApiKey(): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun providesApiKeyInterceptor(@NetworkApiKey apiKey: String): ApiKeyInterceptor =
        ApiKeyInterceptor(apiKey)

    @Provides
    @Singleton
    fun providesOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(apiKeyInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Provides
    @Singleton
    fun providesNetworkService(retrofit: Retrofit): NetworkService =
        retrofit.create(NetworkService::class.java)

    @Provides
    @Singleton
    fun providesDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun providesNetworkHelper(@ApplicationContext context: Context): NetworkHelper =
        NetworkHelperImpl(context)

    @Provides
    @Singleton
    fun providesLoggerService(): LoggerService = LoggerServiceImpl()
}