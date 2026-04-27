package com.gsm.newscircle.data

import app.cash.turbine.test
import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.model.topheadline.ApiSource
import com.gsm.newscircle.data.model.topheadline.TopHeadlinesResponse
import com.gsm.newscircle.data.repository.NewsListRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewsListRepositoryTest {
    @Mock
    lateinit var networkService: NetworkService

    lateinit var newsListRepository: NewsListRepository

    @Before
    fun setUp() {
        newsListRepository = NewsListRepository(networkService)
    }

    @Test
    fun getAllNewsBySource_whenNetworkServiceResponseIsSuccess_shouldReturnSuccess() = runTest {
        val apiSource = ApiSource("sourceId", "sourceName")
        val apiArticle = ApiArticle(
            "articleAuthor",
            "articleContent",
            "articleDescription",
            "articlePublishedDate",
            apiSource,
            "articleTitle",
            "articleUrl",
            "articleImageUrl"
        )
        val apiArticles = mutableListOf<ApiArticle>()
        apiArticles.add(apiArticle)
        val topHeadlineResponse = TopHeadlinesResponse(apiArticles, "ok", 1)

        doReturn(topHeadlineResponse).`when`(networkService).getNewsBySource("sourceId")

        newsListRepository.getAllNewsBySource("sourceId").test {
            assertEquals(topHeadlineResponse.apiArticles, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkService, times(1)).getNewsBySource("sourceId")
    }

    @Test
    fun getAllNewsBySource_whenNetworkServiceResponseIsError_shouldReturnError() = runTest {
        val errorMessage = "Unknown Error, Something went wrong!"

        doThrow(RuntimeException(errorMessage))
            .`when`(networkService)
            .getNewsBySource("sourceId")

        newsListRepository.getAllNewsBySource("sourceId").test {
            assertEquals(errorMessage, awaitError().message)
            cancelAndIgnoreRemainingEvents()
        }

        verify(networkService, times(1)).getNewsBySource("sourceId")
    }
}