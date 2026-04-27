package com.gsm.newscircle.data

import app.cash.turbine.test
import com.gsm.newscircle.data.api.NetworkService
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.data.model.topheadline.ApiSource
import com.gsm.newscircle.data.model.topheadline.TopHeadlinesResponse
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.utils.AppConstants.COUNTRY
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
class TopHeadlineRepositoryTest {
    @Mock
    lateinit var networkService: NetworkService

    private lateinit var topHeadlineRepository: TopHeadlineRepository

    @Before
    fun setup() {
        topHeadlineRepository = TopHeadlineRepository(networkService)
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseIsSuccessful_shouldReturnSuccess() = runTest {
        val source = ApiSource(
            id = "sourceId", name = "sourceName"
        )
        val article = ApiArticle(
            title = "articleTitle",
            description = "articleDescription",
            url = "articleUrl",
            author = "articleAuthor",
            content = "content",
            publishedAt = "publishedAt",
            urlToImage = "articleImageUrl",
            apiSource = source
        )
        val articles = mutableListOf<ApiArticle>()
        articles.add(article)

        val topHeadlineResponse = TopHeadlinesResponse(
            status = "ok", totalResults = 1, apiArticles = articles
        )

        doReturn(topHeadlineResponse).`when`(networkService).getTopHeadlines(COUNTRY)

        topHeadlineRepository.getTopHeadlines(COUNTRY).test {
            assertEquals(topHeadlineResponse.apiArticles, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify(networkService, times(1)).getTopHeadlines(COUNTRY)
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseIsError_shouldReturnError() = runTest {
        val errorMessage = "Top Headlines with Error"

        doThrow(RuntimeException(errorMessage))
            .`when`(networkService)
            .getTopHeadlines(COUNTRY)

        topHeadlineRepository.getTopHeadlines(COUNTRY).test {
            assertEquals(errorMessage, awaitError().message)
            cancelAndIgnoreRemainingEvents()
        }
        verify(networkService, times(1)).getTopHeadlines(COUNTRY)
    }
}