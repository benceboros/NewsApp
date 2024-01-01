package com.example.newsapp.model.data.local.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.newsapp.model.data.local.NewsAppDatabase
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
@SmallTest
class NewsDaoTest {
    private lateinit var newsDatabase: NewsAppDatabase
    private lateinit var newsDao: NewsDao

    private lateinit var testNewsEntity1: NewsEntity
    private lateinit var testNewsEntity2: NewsEntity
    private lateinit var testNewsEntity3: NewsEntity

    private lateinit var newsEntityList: List<NewsEntity>

    @Before
    fun setup() {
        newsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsAppDatabase::class.java
        ).allowMainThreadQueries().build()

        newsDao = newsDatabase.newsDao()

        testNewsEntity1 = NewsEntity(
            id = 1,
            imageUrl = "imageUrl1",
            title = "title1",
            description = "description1",
            publishDate = "publishDate1",
            author = "author1",
            urlToArticle = "url1"
        )

        testNewsEntity2 = NewsEntity(
            id = 2,
            imageUrl = "imageUrl2",
            title = "title2",
            description = "description2",
            publishDate = "publishDate2",
            author = "author2",
            urlToArticle = "url2"
        )

        testNewsEntity3 = NewsEntity(
            id = 3,
            imageUrl = "imageUrl3",
            title = "title3",
            description = "description3",
            publishDate = "publishDate3",
            author = "author3",
            urlToArticle = "url3"
        )

        newsEntityList = listOf(testNewsEntity1, testNewsEntity2, testNewsEntity3)
    }

    @After
    fun cleanup() {
        newsDatabase.close()
    }

    @Test
    fun `Inserting a list of NewsEntity to the database test`() = runTest {
        val initialNewsEntitiesInDb = newsDao.getNews()
        assertThat(initialNewsEntitiesInDb).isEmpty()

        newsDao.insertNewsEntities(newsEntityList)

        val newsEntitiesInDbAfterInsertion = newsDao.getNews()
        assertThat(newsEntitiesInDbAfterInsertion).hasSize(newsEntityList.size)
        assertThat(newsEntitiesInDbAfterInsertion).contains(testNewsEntity1)
    }

    @Test
    fun `Get selected NewsEntity from the database test`() = runTest {
        newsDao.insertNewsEntities(newsEntityList)
        assertThat(testNewsEntity1.id).isNotNull()
        val newsEntityInDb = newsDao.getSelectedNewsEntity(testNewsEntity1.id!!)
        assertEquals(testNewsEntity1, newsEntityInDb)
    }

    @Test
    fun `Delete all the entities from the database test`() = runTest {
        newsDao.insertNewsEntities(newsEntityList)
        val newsEntitiesInDbAfterInsertion = newsDao.getNews()
        assertThat(newsEntitiesInDbAfterInsertion).hasSize(newsEntityList.size)

        newsDao.deleteNewsEntities()

        val newsEntitiesInDbAfterDeletion = newsDao.getNews()
        assertThat(newsEntitiesInDbAfterDeletion).isEmpty()
    }
}