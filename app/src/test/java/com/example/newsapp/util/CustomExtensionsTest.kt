package com.example.newsapp.util

import com.example.newsapp.model.data.remote.respones.Article
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Locale

class CustomExtensionsTest {

    private lateinit var testArticle1: Article
    private lateinit var testArticle2: Article
    private lateinit var testArticle3: Article

    @Before
    fun setup() {
        testArticle1 = Article(
            urlToImage = "testUrl",
            title = "testTitle",
            publishedAt = "testPublishedAt",
            author = null,
            content = null,
            description = null,
            source = null,
            url = null
        )
        testArticle2 = Article(
            urlToImage = null,
            title = "testTitle",
            publishedAt = "2023-2-31T18:46:45Z",
            author = null,
            content = null,
            description = null,
            source = null,
            url = null
        )
        testArticle3 = Article(
            urlToImage = null,
            title = null,
            publishedAt = "2023-12-31T18:46:45Z",
            author = null,
            content = null,
            description = null,
            source = null,
            url = null
        )
    }

    @Test
    fun `Check whether the articles can be saved`() {
        assertThat(testArticle1.canBeSaved()).isTrue()
        assertThat(testArticle2.canBeSaved()).isFalse()
        assertThat(testArticle3.canBeSaved()).isFalse()
    }

    @Test
    fun `Check whether the publish dates are formatted appropriately`() {
        // When date cannot be parsed
        val dateFormat1 = testArticle1.publishedAt?.formatToAppropriateDateFormat()
        assertThat(dateFormat1).isNull()
        val dateFormat2 = testArticle2.publishedAt?.formatToAppropriateDateFormat()
        assertThat(dateFormat2).isNull()

        // Correct parsing
        val dateFormat3 = testArticle3.publishedAt?.formatToAppropriateDateFormat()
        assertThat(dateFormat3?.lowercase(Locale.getDefault())).contains("december")
    }
}