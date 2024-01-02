package com.example.newsapp.view.screens

import android.content.Context
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import com.example.newsapp.MainActivity
import com.example.newsapp.viewmodel.InfoScreenViewModel
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import com.example.newsapp.R
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.model.TechnologyItem
import com.example.newsapp.view.InitAppWithNavigationComponents
import org.junit.Test

class InfoScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var viewModel: InfoScreenViewModel
    private lateinit var context: Context

    private lateinit var toggleMenuContentDesc: String
    private lateinit var topAppBarTitleText: String

    private lateinit var informationHeader: String
    private lateinit var informationDesc: String

    private lateinit var technologiesHeader: String
    private lateinit var technologyItems: List<TechnologyItem>
    private lateinit var keyArrowRightContentDesc: String

    private lateinit var contactHeader: String
    private lateinit var contactInformationItems: List<ContactInformation>

    private lateinit var navigationDrawerHeaderTitle: String
    private lateinit var menuItemNewsListText: String
    private lateinit var menuItemNewsListContentDesc: String
    private lateinit var menuItemInfoText: String
    private lateinit var menuItemInfoContentDesc: String

    private lateinit var pageLoaderContentDesc: String

    @Before
    fun setup() {
        viewModel = composeTestRule.activity.viewModels<InfoScreenViewModel>().value

        context = composeTestRule.activity.baseContext

        toggleMenuContentDesc = composeTestRule.activity.getString(R.string.content_desc_toggle_menu)
        topAppBarTitleText = composeTestRule.activity.getString(R.string.app_name)

        informationHeader = composeTestRule.activity.getString(R.string.header_information)
        informationDesc = composeTestRule.activity.getString(R.string.app_info_desc)

        technologiesHeader = composeTestRule.activity.getString(R.string.header_technologies_used)
        technologyItems = viewModel.getTechnologies(context)
        keyArrowRightContentDesc = composeTestRule.activity.getString(R.string.content_desc_icon_go_to_url)

        contactHeader = composeTestRule.activity.getString(R.string.header_contact)
        contactInformationItems = viewModel.getContactInformation(context)

        navigationDrawerHeaderTitle = composeTestRule.activity.getString(R.string.title_navigation_drawer_header)
        menuItemNewsListText = composeTestRule.activity.getString(R.string.menu_item_news_list)
        menuItemNewsListContentDesc = composeTestRule.activity.getString(R.string.content_desc_go_to_news_list_screen)
        menuItemInfoText = composeTestRule.activity.getString(R.string.menu_item_info)
        menuItemInfoContentDesc = composeTestRule.activity.getString(R.string.content_desc_go_to_information_screen)

        pageLoaderContentDesc = composeTestRule.activity.getString(R.string.page_loader_content_desc)

        composeTestRule.apply {
            activity.setContent {
                InitAppWithNavigationComponents(
                    startDestination = NavigationRoutes.NEWS_LIST_SCREEN.id
                )
            }
            // navigate to InfoScreen
            onNodeWithContentDescription(toggleMenuContentDesc).performClick()
            onNodeWithContentDescription(menuItemInfoContentDesc).performClick()
        }
    }

    private fun checkTopAppBarComponents() {
        composeTestRule.apply {
            onNodeWithContentDescription(toggleMenuContentDesc).assertExists()
            onNodeWithText(topAppBarTitleText).assertExists()
        }
    }

    private fun checkInfoSectionComponents(
        componentsVisible: Boolean
    ) {
        composeTestRule.apply {
            if (componentsVisible) {
                onNodeWithText(informationHeader).assertExists()
                onNodeWithText(informationDesc).assertExists()
            } else {
                onNodeWithText(informationHeader).assertDoesNotExist()
                onNodeWithText(informationDesc).assertDoesNotExist()
            }
        }
    }

    private fun checkTechnologySectionComponents(
        componentsVisible: Boolean
    ) {
        composeTestRule.apply {
            if (componentsVisible) {
                onNodeWithText(technologiesHeader).assertExists()
                technologyItems.forEach { technologyItem ->
                    onNodeWithText(technologyItem.name).assertExists()
                }
                onAllNodesWithContentDescription(keyArrowRightContentDesc).assertCountEquals(technologyItems.size)
            } else {
                onNodeWithText(technologiesHeader).assertDoesNotExist()
                technologyItems.forEach { technologyItem ->
                    onNodeWithText(technologyItem.name).assertDoesNotExist()
                }
                onAllNodesWithContentDescription(keyArrowRightContentDesc).assertCountEquals(0)
            }

        }
    }

    private fun checkContactInformationComponents(
        componentsVisible: Boolean
    ) {
        composeTestRule.apply {
            if (componentsVisible) {
                onNodeWithText(contactHeader).assertExists()
                contactInformationItems.forEach { contactInformationItem ->
                    onNodeWithText("${contactInformationItem.typeName}:").assertExists()
                    onNodeWithText(contactInformationItem.value).assertExists()
                }
            } else {
                onNodeWithText(contactHeader).assertDoesNotExist()
                contactInformationItems.forEach { contactInformationItem ->
                    onNodeWithText("${contactInformationItem.typeName}:").assertDoesNotExist()
                    onNodeWithText(contactInformationItem.value).assertDoesNotExist()
                }
            }
        }
    }

    private fun checkIfMenuIsVisible() {
        composeTestRule.apply {
            onNodeWithText(navigationDrawerHeaderTitle).assertExists()

            onNodeWithText(menuItemNewsListText).assertExists()
            onNodeWithContentDescription(menuItemNewsListContentDesc).assertExists()

            onNodeWithText(menuItemInfoText).assertExists()
            onNodeWithContentDescription(menuItemInfoContentDesc).assertExists()
        }
    }

    private fun checkInfoScreenComponents(
        componentsVisible: Boolean
    ) {
        checkTopAppBarComponents()
        checkInfoSectionComponents(componentsVisible)
        checkTechnologySectionComponents(componentsVisible)
        checkContactInformationComponents(componentsVisible)
    }

    @Test
    fun info_screen_components_exists() {
        checkInfoScreenComponents(componentsVisible = true)
    }

    @Test
    fun check_menu_visibility_after_opening_it() {
        composeTestRule.onNodeWithContentDescription(toggleMenuContentDesc).performClick()
        checkIfMenuIsVisible()
    }

    @Test
    fun navigation_to_news_list_screen() {
        composeTestRule.apply {
            checkInfoScreenComponents(componentsVisible = true)
            onNodeWithContentDescription(toggleMenuContentDesc).performClick()
            onNodeWithContentDescription(menuItemNewsListContentDesc).performClick()
            checkInfoScreenComponents(componentsVisible = false)
            onNodeWithContentDescription(pageLoaderContentDesc).assertExists()
        }
    }

    @Test
    fun navigation_to_news_list_screen_with_back_press() {
        composeTestRule.apply {
            checkInfoScreenComponents(componentsVisible = true)
            Espresso.pressBack()
            checkInfoScreenComponents(componentsVisible = false)
            onNodeWithContentDescription(pageLoaderContentDesc).assertExists()
        }
    }
}