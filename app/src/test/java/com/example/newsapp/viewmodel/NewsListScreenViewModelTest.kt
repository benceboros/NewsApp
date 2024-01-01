package com.example.newsapp.viewmodel

import com.example.newsapp.TestDispatcherRule
import com.example.newsapp.model.data.local.repositories.MockNewsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsListScreenViewModelTest {
    private lateinit var viewModel: NewsListScreenViewModel
    private lateinit var newsRepository: MockNewsRepository

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Before
    fun setup() {
        newsRepository = MockNewsRepository()
        viewModel = NewsListScreenViewModel(
            newsRepository = newsRepository
        )
    }

    private fun loadNewsInitially() = runTest {
        // The initial news list is empty
        assertThat(viewModel.newsList.value).isEmpty()
        viewModel.loadNewsListPaginated()
        // Initial loading state is true because the newsList is empty
        assertThat(viewModel.isLoadingInitialNews.value).isTrue()
        // Wait for the loading delay
        delay(1000)
    }

    @Test
    fun `Successful query of the news list`() {
        // Load the news list with successful result
        loadNewsInitially()

        // The query succeeded and the loading is stopped
        assertThat(viewModel.isLoadingInitialNews.value).isFalse()
        // There is no error and newsList is equal to the repository's list
        assertThat(viewModel.loadError.value).isFalse()
        assertThat(viewModel.newsList.value).isEqualTo(newsRepository.newsList)
    }

    @Test
    fun `Failed query of the news list` () = runTest {
        // Load the news list with error result
        newsRepository.shouldReturnNetworkError(true)
        loadNewsInitially()

        // The query failed and the loading is stopped
        delay(1000)
        assertThat(viewModel.isLoadingInitialNews.value).isFalse()
        // loadError state is true and newsList remains empty
        assertThat(viewModel.loadError.value).isTrue()
        assertThat(viewModel.newsList.value).isEmpty()
    }

    @Test
    fun `Loading the offline news when there are no entries in the database`() {
        assertThat(viewModel.newsList.value).isEmpty()
        viewModel.loadDbSavedNews()
        assertThat(viewModel.noOfflineNews.value).isTrue()
        assertThat(viewModel.newsList.value).isEmpty()
    }

    @Test
    fun `Loading the offline news when there are entries in the database`() {
        // Load the news successfully and save them to the database
        loadNewsInitially()
        // The news list in the database is not empty
        assertThat(newsRepository.newsList).isNotEmpty()

        // Reset the news list in the viewModel
        viewModel.newsList.value = listOf()
        assertThat(viewModel.newsList.value).isEmpty()

        // Load the database saved news list to the viewModel
        viewModel.loadDbSavedNews()
        assertThat(viewModel.noOfflineNews.value).isFalse()
        assertThat(viewModel.newsList.value).isNotEmpty()
        assertThat(viewModel.newsList.value).isEqualTo(newsRepository.newsList)
    }

    @Test
    fun `Successful refresh of the news list`() = runTest {
        viewModel.refreshNews()

        assertThat(viewModel.refreshing.value).isTrue()
        assertThat(viewModel.loadError.value).isFalse()

        // Waiting for refreshing the list
        delay(1000)

        // Refreshing stopped with no loading error
        assertThat(viewModel.refreshing.value).isFalse()
        assertThat(viewModel.loadError.value).isFalse()
    }

    @Test
    fun `Unsuccessful refresh of the news list`() = runTest {
        newsRepository.shouldReturnNetworkError(true)
        viewModel.refreshNews()

        assertThat(viewModel.refreshing.value).isTrue()
        assertThat(viewModel.loadError.value).isFalse()

        // Waiting for refreshing the list
        delay(1000)

        // Wait for the error loading delay
        delay(1000)

        // Refreshing stopped, but there was a loading error
        assertThat(viewModel.refreshing.value).isFalse()
        assertThat(viewModel.loadError.value).isTrue()
    }

    @Test
    fun `Closing the offline news dialog test`() {
        viewModel.closeNoOfflineNewsDialog()
        assertFalse(viewModel.noOfflineNews.value)
    }
}