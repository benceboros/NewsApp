package com.example.newsapp.view.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.R
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.ContactInformationType
import com.example.newsapp.model.TechnologyItem
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.contactInfoHeaderStyle
import com.example.newsapp.ui.theme.contactInfoStyle
import com.example.newsapp.util.LocalAnalyticsHelper
import com.example.newsapp.util.analytics.AnalyticsHelper
import com.example.newsapp.util.analytics.TrackScreenViewEvent
import com.example.newsapp.util.analytics.logContentSelect
import com.example.newsapp.util.navigateToUrl
import com.example.newsapp.view.Routes
import com.example.newsapp.viewmodel.InfoScreenViewModel

@Composable
fun InfoScreen(
    navController: NavController,
    viewModel: InfoScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val analyticsHelper = LocalAnalyticsHelper.current

    TrackScreenViewEvent(screenName = Routes.INFO_SCREEN.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoHeader(
            modifier = Modifier.padding(bottom = 16.dp),
            headerTitle = stringResource(R.string.header_information)
        )
        Text(
            text = stringResource(R.string.app_info_desc),
            fontSize = 15.sp,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
        )

        InfoHeader(headerTitle = stringResource(R.string.header_technologies_used))
        TechnologyList(
            navController = navController,
            technologyList = viewModel.getTechnologies(context),
            analyticsHelper = analyticsHelper
        )

        InfoHeader(headerTitle = stringResource(R.string.header_contact))
        ContactInformation(
            navController = navController,
            contactInfoItems = viewModel.getContactInformation(context),
            openDialer = viewModel::openDialer,
            openEmailApp = viewModel::openEmailApp,
            analyticsHelper = analyticsHelper
        )
    }
}

@Composable
fun InfoHeader(
    modifier: Modifier = Modifier,
    headerTitle: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = headerTitle,
            style = contactInfoHeaderStyle
        )
    }
}

@Composable
fun TechnologyList(
    navController: NavController,
    technologyList: List<TechnologyItem>,
    analyticsHelper: AnalyticsHelper
) {
    Column {
        technologyList.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        analyticsHelper.logContentSelect(
                            contentType = "technology website",
                            itemId = item.name
                        )
                        navController.navigateToUrl(item.websiteUrl)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.content_desc_icon_go_to_url)
                )
            }
            if (item != technologyList.last()) {
                ItemDivider()
            }
        }
    }
}

@Composable
fun ContactInformation(
    navController: NavController,
    contactInfoItems: List<ContactInformation>,
    openDialer: (context: Context, phoneNumber: String) -> Unit,
    openEmailApp: (context: Context, emailAddress: String) -> Unit,
    analyticsHelper: AnalyticsHelper
) {
    val context = LocalContext.current
    Column {
        contactInfoItems.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        when (item.type) {
                            ContactInformationType.PHONE -> {
                                analyticsHelper.logContentSelect(
                                    contentType = "phone number",
                                    itemId = item.typeName
                                )
                                openDialer(context, item.value)
                            }

                            ContactInformationType.EMAIL -> {
                                analyticsHelper.logContentSelect(
                                    contentType = "email address",
                                    itemId = item.typeName
                                )
                                openEmailApp(context, item.value)
                            }

                            ContactInformationType.LINKEDIN -> {
                                analyticsHelper.logContentSelect(
                                    contentType = "linkedin page",
                                    itemId = item.typeName
                                )
                                navController.navigateToUrl(item.value)
                            }
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.typeName}:",
                    fontSize = 18.sp
                )
                Text(
                    text = item.value,
                    style = contactInfoStyle,
                    fontSize = if (item.type == ContactInformationType.LINKEDIN) 13.sp else 16.sp
                )
            }
            if (item != contactInfoItems.last()) {
                ItemDivider()
            }
        }
    }
}

@Composable
fun ItemDivider() {
    Divider(
        color = MaterialTheme.colorScheme.secondary,
        thickness = 1.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun InfoScreenPreview() {
    val navController = rememberNavController()
    NewsAppTheme {
        InfoScreen(navController)
    }
}