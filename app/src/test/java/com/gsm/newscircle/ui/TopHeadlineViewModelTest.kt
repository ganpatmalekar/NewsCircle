package com.gsm.newscircle.ui

import app.cash.turbine.test
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.topheadline.TopHeadlineViewModel
import com.gsm.newscircle.utils.AppConstants.COUNTRY
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
class TopHeadlineViewModelTest {
    @Mock
    lateinit var topHeadlineRepository: TopHeadlineRepository

    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkHelper: NetworkHelper
    private lateinit var loggerService: LoggerService

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        networkHelper = TestNetworkHelper()
        loggerService = TestLoggerService()
    }

    @Suppress("UnusedFlow")
    @Test
    fun fetchTopHeadlines_whenRepositoryResponseIsSuccess_shouldReturnSuccessUiState() = runTest {
        val articles = emptyList<ApiArticle>()

        doReturn(flowOf(articles))
            .`when`(topHeadlineRepository)
            .getTopHeadlines(COUNTRY)

        val topHeadlineViewModel = TopHeadlineViewModel(
            topHeadlineRepository,
            dispatcherProvider,
            networkHelper,
            loggerService
        )

        // The init block in TopHeadlineViewModel already calls fetchTopHeadlines() that's why
        // no need to explicitly call fetchTopHeadlines() here

        topHeadlineViewModel.topHeadlineUiState.test {
            assertEquals(UiState.Success(emptyList<ApiArticle>()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadlineRepository, times(1)).getTopHeadlines(COUNTRY)
    }

    @Suppress("UnusedFlow")
    @Test
    fun fetchNews_whenRepositoryResponseIsError_shouldReturnErrorUiState() = runTest {
        val errorMessage = "Unknown Error, Something went wrong!"

        doReturn(flow<List<ApiArticle>> {
            throw IllegalStateException(errorMessage)
        })
            .`when`(topHeadlineRepository)
            .getTopHeadlines(COUNTRY)

        val topHeadlineViewModel = TopHeadlineViewModel(
            topHeadlineRepository,
            dispatcherProvider,
            networkHelper,
            loggerService
        )

        topHeadlineViewModel.topHeadlineUiState.test {
            assertEquals(UiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(topHeadlineRepository, times(1)).getTopHeadlines(COUNTRY)
    }
}