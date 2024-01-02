package com.example.newsapp.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.ui.theme.newsDateAndAuthorStyle
import com.example.newsapp.ui.theme.newsDescriptionStyle
import com.example.newsapp.ui.theme.newsTitleDetailStyle
import com.example.newsapp.util.LocalAnalyticsHelper
import com.example.newsapp.util.analytics.TrackScreenViewEvent
import com.example.newsapp.util.analytics.logButtonClick
import com.example.newsapp.util.navigateToUrl
import com.example.newsapp.view.PageLoader
import com.example.newsapp.view.Routes
import com.example.newsapp.viewmodel.NewsDetailsScreenEvent
import com.example.newsapp.viewmodel.NewsDetailsScreenViewModel

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    newsId: Int,
    viewModel: NewsDetailsScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(NewsDetailsScreenEvent.NewsItemSelected(newsId))
    }

    TrackScreenViewEvent(screenName = Routes.NEWS_DETAILS_SCREEN.id)

    if (viewModel.state.selectedNewsEntity != null) {
        DisplayNewsItemDetails(newsEntity = viewModel.state.selectedNewsEntity, navController = navController)
    } else {
        PageLoader()
    }
}

@Composable
fun DisplayNewsItemDetails(newsEntity: NewsEntity?, navController: NavController) {
    var imageIsLoading by remember { mutableStateOf(false) }
    val analyticsHelper = LocalAnalyticsHelper.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (imageIsLoading) {
                CircularProgressIndicator()
            }
            AsyncImage(
                modifier = if (imageIsLoading) {
                    Modifier
                        .height(200.dp)
                } else {
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                },
                model = newsEntity?.imageUrl,
                contentDescription = stringResource(R.string.content_desc_image_of_news_article),
                onLoading = { imageIsLoading = true },
                onSuccess = { imageIsLoading = false },
                onError = { imageIsLoading = false },
                error = painterResource(id = R.drawable.ic_broken_image)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = newsEntity?.title ?: stringResource(R.string.unknown_title),
                style = newsTitleDetailStyle
            )
            Text(
                text = newsEntity?.description ?: stringResource(R.string.unknown_description),
                style = newsDescriptionStyle,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = newsEntity?.author ?: stringResource(R.string.unknown_author),
                style = newsDateAndAuthorStyle,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 4.dp)
            )
            Text(
                text = newsEntity?.publishDate ?: stringResource(R.string.unknown_publish_date),
                style = newsDateAndAuthorStyle,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 4.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    analyticsHelper.logButtonClick(
                        buttonId = "read_full_article_online_button"
                    )
                    navController.navigateToUrl(newsEntity?.urlToArticle)
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(text = stringResource(R.string.btn_read_full_article_online).uppercase())
            }
        }
    }
}
