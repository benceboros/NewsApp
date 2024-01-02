package com.example.newsapp.view.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import com.example.newsapp.MainActivity
import com.example.newsapp.UITestManager
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.view.InitAppWithNavigationComponents
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NewsDetailsScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var uiTestManager: UITestManager
    private var screenLoadedSuccessfully: Boolean = false

    @Before
    fun setup() {
        uiTestManager = UITestManager(composeTestRule)

        composeTestRule.activity.setContent {
            InitAppWithNavigationComponents(
                startDestination = NavigationRoutes.NEWS_LIST_SCREEN.id
            )
        }
        screenLoadedSuccessfully = uiTestManager.navigateToNewsDetailsScreenIfPossible()
    }

    @Test
    fun news_details_components_exists() {
        uiTestManager.apply {
            if (screenLoadedSuccessfully) {
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = true, shouldToggleMenuBeVisible = false)
            }
        }
    }

    @Test
    fun navigation_to_news_list_screen_with_back_menu_item() {
        uiTestManager.apply {
            if (screenLoadedSuccessfully) {
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = true, shouldToggleMenuBeVisible = false)
                navigateBackWithMenuItem()
                checkNewsListScreenComponents(shouldComponentsBeVisible = true, shouldPageLoaderBeVisible = false)
            }
        }
    }

    @Test
    fun navigation_to_news_list_screen_with_back_press() {
        uiTestManager.apply {
            if (screenLoadedSuccessfully) {
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = true, shouldToggleMenuBeVisible = false)
                Espresso.pressBack()
                checkNewsListScreenComponents(shouldComponentsBeVisible = true, shouldPageLoaderBeVisible = false)
            }
        }
    }

    @Test
    fun navigation_to_article_website() {
        uiTestManager.apply {
            if (screenLoadedSuccessfully) {
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = true, shouldToggleMenuBeVisible = false)
                navigateToArticleWebsite()
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = false, shouldToggleMenuBeVisible = false)
            }
        }
    }
}