package com.example.newsapp

import androidx.activity.viewModels
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.TechnologyItem
import com.example.newsapp.viewmodel.InfoScreenViewModel
import com.example.newsapp.viewmodel.NewsListScreenViewModel
import javax.inject.Inject

class UITestManager(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {
    @Inject
    lateinit var newsListScreenViewModel: NewsListScreenViewModel

    @Inject
    lateinit var infoScreenViewModel: InfoScreenViewModel

    private val context = composeTestRule.activity.baseContext

    private val pageLoaderContentDesc = composeTestRule.activity.getString(R.string.content_desc_page_loader)

    /**
     * Components related to top app bar and the menu
     */
    private val toggleMenuContentDesc = composeTestRule.activity.getString(R.string.content_desc_toggle_menu)
    private val goBackToPreviousScreenContentDesc = composeTestRule.activity.getString(R.string.content_desc_go_back_to_previous_screen)
    private val topAppBarTitleText = composeTestRule.activity.getString(R.string.app_name)
    private val navigationDrawerHeaderTitle = composeTestRule.activity.getString(R.string.title_navigation_drawer_header)
    private val menuItemNewsListText = composeTestRule.activity.getString(R.string.menu_item_news_list)
    private val menuItemNewsListContentDesc = composeTestRule.activity.getString(R.string.content_desc_go_to_news_list_screen)
    private val menuItemInfoText = composeTestRule.activity.getString(R.string.menu_item_info)
    private val menuItemInfoContentDesc = composeTestRule.activity.getString(R.string.content_desc_go_to_information_screen)

    /**
     * Components related to NewsListScreen when there are no news articles in the list
     */
    private val noNetworkIconContentDesc = composeTestRule.activity.getString(R.string.content_desc_no_network_icon)
    private val newsCouldNotBeLoadedText = composeTestRule.activity.getString(R.string.news_could_not_be_loaded)
    private val retryButtonContentDesc = composeTestRule.activity.getString(R.string.content_desc_btn_retry)
    private val useOfflineButtonContentDesc = composeTestRule.activity.getString(R.string.content_desc_use_offline)

    /**
     * Components related to News entities
     */
    private val clickableNewsArticleContentDesc = composeTestRule.activity.getString(R.string.content_desc_clickable_news_article)
    private val newsEntityImageContainerContentDesc = composeTestRule.activity.getString(R.string.content_desc_image_container)
    private val newsEntityTitleContentDesc = composeTestRule.activity.getString(R.string.content_desc_news_article_title)
    private val newsEntityPublishDateContentDesc = composeTestRule.activity.getString(R.string.content_desc_news_article_publish_date)
    private val newsEntityDescriptionContentDesc = composeTestRule.activity.getString(R.string.content_desc_news_article_description)
    private val newsEntityAuthorContentDesc = composeTestRule.activity.getString(R.string.content_desc_news_article_author)
    private val newsEntityReadFullArticleOnlineButtonContentDesc = composeTestRule.activity.getString(R.string.content_desc_news_article_author)

    /**
     * Components related to InfoScreen
     */
    private val informationHeader = composeTestRule.activity.getString(R.string.header_information)
    private val informationDesc = composeTestRule.activity.getString(R.string.app_info_desc)
    private val technologiesHeader = composeTestRule.activity.getString(R.string.header_technologies_used)
    private val keyArrowRightContentDesc = composeTestRule.activity.getString(R.string.content_desc_icon_go_to_url)
    private val contactHeader = composeTestRule.activity.getString(R.string.header_contact)
    private var technologyItems: List<TechnologyItem>
    private var contactInformationItems: List<ContactInformation>

    init {
        newsListScreenViewModel = composeTestRule.activity.viewModels<NewsListScreenViewModel>().value

        infoScreenViewModel = composeTestRule.activity.viewModels<InfoScreenViewModel>().value
        technologyItems = infoScreenViewModel.getTechnologies(context)
        contactInformationItems = infoScreenViewModel.getContactInformation(context)
    }

    /**
     * Functions used in test cases related to the top app bar and the menu
     */
    private fun checkTopAppBarComponents(
        shouldToggleMenuBeVisible: Boolean
    ) {
        composeTestRule.apply {
            onNodeWithText(topAppBarTitleText).assertExists()
            if (shouldToggleMenuBeVisible) {
                onNodeWithContentDescription(toggleMenuContentDesc).assertExists()
            } else {
                onNodeWithContentDescription(goBackToPreviousScreenContentDesc).assertExists()
            }
        }
    }

    fun checkIfMenuIsVisible() {
        openMenu()
        composeTestRule.apply {
            onNodeWithText(navigationDrawerHeaderTitle).assertExists()

            onNodeWithText(menuItemNewsListText).assertExists()
            onNodeWithContentDescription(menuItemNewsListContentDesc).assertExists()

            onNodeWithText(menuItemInfoText).assertExists()
            onNodeWithContentDescription(menuItemInfoContentDesc).assertExists()
        }
    }

    private fun openMenu() {
        composeTestRule.onNodeWithContentDescription(toggleMenuContentDesc).performClick()
    }

    fun navigateBackWithMenuItem() {
        composeTestRule.onNodeWithContentDescription(goBackToPreviousScreenContentDesc).performClick()
    }

    fun navigateToInfoScreen() {
        openMenu()
        composeTestRule.onNodeWithContentDescription(menuItemInfoContentDesc).performClick()
    }

    fun navigateToNewsListScreen() {
        openMenu()
        composeTestRule.onNodeWithContentDescription(menuItemNewsListContentDesc).performClick()
    }


    /**
     * Functions used in test cases related to NewsListScreen
     */
    fun checkNewsListScreenComponents(
        shouldComponentsBeVisible: Boolean,
        shouldToggleMenuBeVisible: Boolean = true,
        shouldPageLoaderBeVisible: Boolean = true
    ) {
        checkTopAppBarComponents(shouldToggleMenuBeVisible)
        checkPageLoader(shouldPageLoaderBeVisible)
        // Giving time for loading the items
        Thread.sleep(2500)
        if (newsListScreenViewModel.newsList.value.isEmpty()) {
            checkNoInternetWithNoNewsIsDisplayed(shouldComponentsBeVisible)
        } else {
            checkNewsListIsDisplayed(shouldComponentsBeVisible)
        }
    }

    private fun checkPageLoader(shouldPageLoaderBeVisible: Boolean) {
        if (shouldPageLoaderBeVisible) {
            composeTestRule.onNodeWithContentDescription(pageLoaderContentDesc).assertExists()
        } else {
            composeTestRule.onNodeWithContentDescription(pageLoaderContentDesc).assertDoesNotExist()
        }
    }

    fun navigateToNewsDetailsScreenIfPossible(): Boolean {
        composeTestRule.apply {
            return if (newsListScreenViewModel.newsList.value.isNotEmpty()) {
                onAllNodesWithContentDescription(clickableNewsArticleContentDesc)[0].performClick()
                checkNewsDetailsScreenComponents(shouldComponentsBeVisible = true, shouldToggleMenuBeVisible = false)
                true
            } else {
                false
            }
        }
    }

    private fun checkNewsListIsDisplayed(
        shouldComponentsBeVisible: Boolean
    ) {
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
                // At least one item is visible
                onAllNodesWithContentDescription(clickableNewsArticleContentDesc)[0].assertExists()
                onAllNodesWithContentDescription(newsEntityImageContainerContentDesc)[0].assertExists()
                onAllNodesWithContentDescription(newsEntityTitleContentDesc)[0].assertExists()
                onAllNodesWithContentDescription(newsEntityPublishDateContentDesc)[0].assertExists()
            } else {
                onAllNodesWithContentDescription(clickableNewsArticleContentDesc).assertCountEquals(0)
                onAllNodesWithContentDescription(newsEntityImageContainerContentDesc).assertCountEquals(0)
                onAllNodesWithContentDescription(newsEntityTitleContentDesc).assertCountEquals(0)
                onAllNodesWithContentDescription(newsEntityPublishDateContentDesc).assertCountEquals(0)
            }
        }
    }

    private fun checkNoInternetWithNoNewsIsDisplayed(
        shouldComponentsBeVisible: Boolean
    ) {
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
                onNodeWithText(newsCouldNotBeLoadedText).assertExists()
                onNodeWithContentDescription(noNetworkIconContentDesc).assertExists()
                onNodeWithContentDescription(retryButtonContentDesc).assertExists()
                onNodeWithContentDescription(useOfflineButtonContentDesc).assertExists()
            } else {
                onNodeWithText(newsCouldNotBeLoadedText).assertDoesNotExist()
                onNodeWithContentDescription(noNetworkIconContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(retryButtonContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(useOfflineButtonContentDesc).assertDoesNotExist()
            }
        }
    }

    /**
     * Functions used in test cases related to InfoScreen
     */
    fun checkInfoScreenComponents(
        shouldComponentsBeVisible: Boolean,
        shouldToggleMenuBeVisible: Boolean = true
    ) {
        checkTopAppBarComponents(shouldToggleMenuBeVisible)
        checkInfoSectionComponents(shouldComponentsBeVisible)
        checkTechnologySectionComponents(shouldComponentsBeVisible)
        checkContactInformationComponents(shouldComponentsBeVisible)
    }

    private fun checkInfoSectionComponents(
        shouldComponentsBeVisible: Boolean
    ) {
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
                onNodeWithText(informationHeader).assertExists()
                onNodeWithText(informationDesc).assertExists()
            } else {
                onNodeWithText(informationHeader).assertDoesNotExist()
                onNodeWithText(informationDesc).assertDoesNotExist()
            }
        }
    }

    private fun checkTechnologySectionComponents(
        shouldComponentsBeVisible: Boolean
    ) {
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
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
        shouldComponentsBeVisible: Boolean
    ) {
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
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

    /**
     * Functions used in test cases related to NewsDetailsScreen
     */
    fun checkNewsDetailsScreenComponents(
        shouldComponentsBeVisible: Boolean,
        shouldToggleMenuBeVisible: Boolean
    ) {
        checkTopAppBarComponents(shouldToggleMenuBeVisible)
        composeTestRule.apply {
            if (shouldComponentsBeVisible) {
                onNodeWithContentDescription(newsEntityImageContainerContentDesc).assertExists()
                onNodeWithContentDescription(newsEntityTitleContentDesc).assertExists()
                onNodeWithContentDescription(newsEntityDescriptionContentDesc).assertExists()
                onNodeWithContentDescription(newsEntityAuthorContentDesc).assertExists()
                onNodeWithContentDescription(newsEntityPublishDateContentDesc).assertExists()
                onNodeWithContentDescription(newsEntityReadFullArticleOnlineButtonContentDesc).assertExists()
            } else {
                onNodeWithContentDescription(newsEntityImageContainerContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(newsEntityTitleContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(newsEntityDescriptionContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(newsEntityAuthorContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(newsEntityPublishDateContentDesc).assertDoesNotExist()
                onNodeWithContentDescription(newsEntityReadFullArticleOnlineButtonContentDesc).assertDoesNotExist()
            }
        }
    }

    fun navigateToArticleWebsite() {
        composeTestRule.onNodeWithContentDescription(newsEntityReadFullArticleOnlineButtonContentDesc).performClick()
    }
}