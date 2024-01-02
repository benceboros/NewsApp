package com.example.newsapp.view.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import com.example.newsapp.MainActivity
import org.junit.Before
import org.junit.Rule
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.UITestManager
import com.example.newsapp.view.InitAppWithNavigationComponents
import org.junit.Test

class InfoScreenTest {
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
        uiTestManager.navigateToInfoScreen()
    }

    @Test
    fun info_screen_components_exists() {
        uiTestManager.checkInfoScreenComponents(shouldComponentsBeVisible = true)
    }

    @Test
    fun check_menu_visibility_after_opening_it() {
        uiTestManager.checkIfMenuIsVisible()
    }

    @Test
    fun navigation_to_news_list_screen() {
        uiTestManager.apply {
            checkInfoScreenComponents(shouldComponentsBeVisible = true)
            navigateToNewsListScreen()
            checkInfoScreenComponents(shouldComponentsBeVisible = false)
            checkNewsListScreenComponents(shouldComponentsBeVisible = true)
        }
    }

    @Test
    fun navigation_to_news_list_screen_with_back_press() {
        uiTestManager.apply {
            checkInfoScreenComponents(shouldComponentsBeVisible = true)
            Espresso.pressBack()
            checkInfoScreenComponents(shouldComponentsBeVisible = false)
            checkNewsListScreenComponents(shouldComponentsBeVisible = true)
        }
    }
}