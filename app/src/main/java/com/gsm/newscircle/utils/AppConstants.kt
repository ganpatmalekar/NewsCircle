package com.gsm.newscircle.utils

object AppConstants {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val COUNTRY = "us"
    const val LANGUAGE = "en"
    const val API_KEY_HEADER = "X-Api-Key"
    const val DB_NAME = "ArticlesDB"
    const val NEWS_BY_SOURCES = "sources"
    const val DEFAULT_QUERY = "World"
    const val SEARCH_IN = "title"
    const val SORT_BY_OPTION = "publishedAt"
    const val DEBOUNCE_TIME = 300L
    const val NO_ARTICLES = "no articles"
    const val NO_SOURCES = "no sources"
    const val NO_INTERNET_MSG = "Please check your internet connection and try again."
    const val BLANK_QUERY_MSG = "No articles, enter at least one character to search."
    // Pagination
    const val INITIAL_PAGE = 1
    const val PAGE_SIZE = 20
    const val TOP_HEADLINE_VIEWMODEL_TAG = "TopHeadlineViewModel"
    const val NEWS_SOURCE_VIEWMODEL_TAG = "NewsSourceViewModel"
    const val NEWS_LIST_VIEWMODEL_TAG = "NewsListViewModel"
    const val COUNTRY_LIST_VIEWMODEL_TAG = "CountryListViewModel"
    const val LANGUAGE_LIST_VIEWMODEL_TAG = "LanguageListViewModel"
    const val SEARCH_NEWS_VIEWMODEL_TAG = "SearchNewsViewModel"
    const val TOP_HEADLINE_OFFLINE_VIEWMODEL_TAG = "TopHeadlineOfflineViewModel"
}