package com.example.newsapp.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.model.NewsItem
import com.example.newsapp.navigateToUrl
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.newsDateAndAuthorStyle
import com.example.newsapp.ui.theme.newsDescriptionStyle
import com.example.newsapp.ui.theme.newsTitleDetailStyle
import com.example.newsapp.viewmodel.NewsDetailsScreenViewModel

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    viewModel: NewsDetailsScreenViewModel = hiltViewModel()
) {
    DisplayNewsItemDetails(newsItem = viewModel.selectedNewsItem, navController = navController)
}

@Composable
fun DisplayNewsItemDetails(newsItem: NewsItem, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = newsItem.imageUrl,
            contentDescription = "Image of the selected news"
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = newsItem.title ?: "Title could not be loaded",
                style = newsTitleDetailStyle,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Text(
                text = newsItem.description ?: "Description could not be loaded",
                style = newsDescriptionStyle,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = newsItem.author ?: "Author could not be loaded",
                style = newsDateAndAuthorStyle,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 4.dp)
            )
            Text(
                text = newsItem.publishDate ?: "Publish date could not be loaded",
                style = newsDateAndAuthorStyle,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 4.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigateToUrl(newsItem.urlToArticle)
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "READ FULL ARTICLE ONLINE")
            }
        }
    }
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
fun NewsDetailsScreenPreview() {
    val navController = rememberNavController()
    NewsAppTheme {
        NewsDetailsScreen(navController)
    }
}