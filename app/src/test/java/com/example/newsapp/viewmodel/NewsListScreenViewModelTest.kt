package com.example.newsapp.viewmodel

import com.example.newsapp.TestDispatcherRule
import com.example.newsapp.model.data.local.repositories.MockNewsRepository
import com.example.newsapp.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsListScreenViewModelTest {
    @RelaxedMockK
    private lateinit var viewModel: NewsListScreenViewModel

    @RelaxedMockK
    private lateinit var newsRepository: MockNewsRepository

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        newsRepository = spyk(MockNewsRepository())
        viewModel = NewsListScreenViewModel(
            newsRepository = newsRepository
        )
    }

    private fun verifyFunctionsInvokedForSuccessfulQuery() {
        // Verify that the appropriate functions are called
        coVerify {
            newsRepository.getNewsResponseWithTotalResults(1)
            newsRepository.deleteDb()
            newsRepository.insertNewsEntitiesToDb(viewModel.newsList.value)
            newsRepository.getDbSavedNewsList()

            // A pagination also happens
            newsRepository.getNewsResponseWithTotalResults(2)
            newsRepository.insertNewsEntitiesToDb(viewModel.newsList.value)
            newsRepository.getDbSavedNewsList()
        }

        // Verify that the response is a success with the appropriate data
        coEvery {
            newsRepository.getNewsResponseWithTotalResults(1)
        } returns Resource.Success(
            viewModel.newsList.value to viewModel.newsList.value.size
        )

        // Verify that the deletion of the database only happens 1 time
        coVerify(exactly = 1) { newsRepository.deleteDb() }

        confirmVerified(newsRepository)
    }

    private fun verifyFunctionsInvokedForUnsuccessfulQuery() {
        // Verify that the appropriate functions are called
        coVerify {
            newsRepository.shouldReturnNetworkError(true)
            newsRepository.getNewsResponseWithTotalResults(1)
        }

        // Verify that the response returns with an error
        coEvery {
            newsRepository.getNewsResponseWithTotalResults(1)
        } returns Resource.Error(
            "Error", null
        )

        // Verify that the deletion of the database is not called!
        coVerify(exactly = 0) { newsRepository.deleteDb() }

        confirmVerified(newsRepository)
    }

    private fun verifyFunctionsInvokedForSuccessfulOfflineQuery() {
        // Verify that the appropriate functions are called
        coVerify {
            newsRepository.getDbSavedNewsList()
            newsRepository.newsList
        }

        // Verify that the response returns with the list
        coEvery {
            newsRepository.getDbSavedNewsList()
        } returns viewModel.newsList.value

        confirmVerified(newsRepository)
    }

    private fun verifyFunctionsInvokedForOfflineQueryWithEmptyDatabase() {
        // Verify that the appropriate functions are called
        coVerify {
            newsRepository.getDbSavedNewsList()
        }

        // Verify that the response returns with an empty list
        coEvery {
            newsRepository.getDbSavedNewsList()
        } returns emptyList()

        confirmVerified(newsRepository)
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

        // Verify that the functions are called appropriately in case of a successful query
        verifyFunctionsInvokedForSuccessfulQuery()

        // The query succeeded and the loading is stopped
        assertThat(viewModel.isLoadingInitialNews.value).isFalse()
        // There is no error and newsList is equal to the repository's list
        assertThat(viewModel.loadError.value).isFalse()
        assertThat(viewModel.newsList.value).isEqualTo(newsRepository.newsList)
    }

    @Test
    fun `Failed query of the news list`() = runTest {
        // Load the news list with error result
        newsRepository.shouldReturnNetworkError(true)
        loadNewsInitially()

        // Verify that the functions are called appropriately in case of a failed query
        verifyFunctionsInvokedForUnsuccessfulQuery()

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

        verifyFunctionsInvokedForOfflineQueryWithEmptyDatabase()

        assertThat(viewModel.noOfflineNews.value).isTrue()
        assertThat(viewModel.newsList.value).isEmpty()
    }

    @Test
    fun `Loading the offline news when there are entries in the database`() {
        // Load the news successfully and save them to the database
        loadNewsInitially()
        verifyFunctionsInvokedForSuccessfulQuery()
        // The news list in the database is not empty
        assertThat(newsRepository.newsList).isNotEmpty()

        // Reset the news list in the viewModel
        viewModel.newsList.value = listOf()
        assertThat(viewModel.newsList.value).isEmpty()

        // Load the database saved news list to the viewModel
        viewModel.loadDbSavedNews()
        verifyFunctionsInvokedForSuccessfulOfflineQuery()

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

        verifyFunctionsInvokedForSuccessfulQuery()

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

        verifyFunctionsInvokedForUnsuccessfulQuery()

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