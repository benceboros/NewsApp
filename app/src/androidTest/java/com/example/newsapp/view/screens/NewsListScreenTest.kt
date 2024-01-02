package com.example.newsapp.view.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import com.example.newsapp.MainActivity
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.view.InitAppWithNavigationComponents
import org.junit.Before
import org.junit.Rule
import com.example.newsapp.UITestManager
import org.junit.Assert
import org.junit.Test


class NewsListScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var uiTestManager: UITestManager

    @Before
    fun setup() {
        uiTestManager = UITestManager(composeTestRule)

        composeTestRule.activity.setContent {
            InitAppWithNavigationComponents(
                startDestination = NavigationRoutes.NEWS_LIST_SCREEN.id
            )
        }
    }

    @Test
    fun news_list_components_exist() {
        uiTestManager.checkNewsListScreenComponents(shouldComponentsBeVisible = true)
    }

    @Test
    fun check_menu_visibility_after_opening_it() {
        uiTestManager.checkIfMenuIsVisible()
    }

    @Test
    fun activity_is_destroyed_after_back_press() {
        Espresso.pressBackUnconditionally()
        Assert.assertEquals(Lifecycle.State.DESTROYED, composeTestRule.activityRule.scenario.state)
    }

    @Test
    fun navigation_to_info_screen() {
        uiTestManager.apply {
            checkNewsListScreenComponents(shouldComponentsBeVisible = true)
            navigateToInfoScreen()
            checkNewsListScreenComponents(shouldComponentsBeVisible = false)
            checkInfoScreenComponents(shouldComponentsBeVisible = true)
        }
    }

    @Test
    fun navigation_to_news_detail_screen_if_possible() {
        uiTestManager.apply {
            checkNewsListScreenComponents(shouldComponentsBeVisible = true)
            navigateToNewsDetailsScreenIfPossible()
        }
    }
}