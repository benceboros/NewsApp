package com.example.newsapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.newsapp.data.local.entities.NewsEntity
import com.example.newsapp.navigateToUrl
import com.example.newsapp.ui.theme.newsDateAndAuthorStyle
import com.example.newsapp.ui.theme.newsDescriptionStyle
import com.example.newsapp.ui.theme.newsTitleDetailStyle
import com.example.newsapp.viewmodel.NewsDetailsScreenEvent
import com.example.newsapp.viewmodel.NewsDetailsScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    newsId: Int,
    viewModel: NewsDetailsScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        delay(1000)
        viewModel.onEvent(NewsDetailsScreenEvent.NewsItemSelected(newsId))
    }

    if (viewModel.state.selectedNewsEntity != null) {
        DisplayNewsItemDetails(newsEntity = viewModel.state.selectedNewsEntity, navController = navController)
    } else {
        PageLoader()
    }
}

@Composable
fun DisplayNewsItemDetails(newsEntity: NewsEntity?, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = newsEntity?.imageUrl,
            contentDescription = stringResource(R.string.content_desc_image_of_the_selected_news),
            error = painterResource(
                id = R.drawable.ic_broken_image
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = newsEntity?.title ?: stringResource(R.string.unknown_title),
                style = newsTitleDetailStyle,
                modifier = Modifier
                    .padding(top = 16.dp)
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
