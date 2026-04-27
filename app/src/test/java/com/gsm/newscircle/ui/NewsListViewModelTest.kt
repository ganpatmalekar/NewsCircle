package com.gsm.newscircle.ui

import app.cash.turbine.test
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.NewsListRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.news.NewsListViewModel
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.TestDispatcherProvider
import com.gsm.newscircle.utils.TestLoggerService
import com.gsm.newscircle.utils.TestNetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsListViewModelTest {
    @Mock
    lateinit var newsListRepository: NewsListRepository

    lateinit var dispatcherProvider: DispatcherProvider
    lateinit var networkHelper: NetworkHelper
    lateinit var loggerService: LoggerService

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        loggerService = TestLoggerService()
    }

    @Suppress("UnusedFlow")
    @Test
    fun getAllNewsBySource_whenRepositoryResponseIsSuccess_shouldReturnSuccessUiState() = runTest {
        val apiArticles = emptyList<ApiArticle>()

        doReturn(flowOf(apiArticles))
            .`when`(newsListRepository)
            .getAllNewsBySource("sourceId")

        val newsListViewModel = NewsListViewModel(
            newsListRepository,
            dispatcherProvider,
            networkHelper,
            loggerService
        )
        // The init block in NewsListViewModel does not call fetchAllNewsBySources("sourceId")
        // that's why call it here explicitly
        newsListViewModel.fetchAllNewsBySources("sourceId")

        newsListViewModel.newsUiState.test {
            assertEquals(UiState.Success(emptyList<ApiArticle>()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(newsListRepository, times(1)).getAllNewsBySource("sourceId")
    }

    @Suppress("UnusedFlow")
    @Test
    fun getAllNewsBySource_whenRepositoryResponseIsError_shouldReturnErrorUiState() = runTest {
        val errorMessage = "Unknown Error, Something Went Wrong!"

        doReturn(flow<List<ApiArticle>> {
            throw IllegalStateException(errorMessage)
        })
            .`when`(newsListRepository)
            .getAllNewsBySource("sourceId")

        val newsListViewModel = NewsListViewModel(
            newsListRepository,
            dispatcherProvider,
            networkHelper,
            loggerService
        )

        newsListViewModel.fetchAllNewsBySources("sourceId")

        newsListViewModel.newsUiState.test {
            assertEquals(UiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(newsListRepository, times(1)).getAllNewsBySource("sourceId")
    }
}