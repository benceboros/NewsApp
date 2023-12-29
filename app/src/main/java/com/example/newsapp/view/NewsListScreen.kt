package com.example.newsapp.view

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.Routes
import com.example.newsapp.model.News
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.newsDatePublishedStyle
import com.example.newsapp.ui.theme.newsTitleListStyle
import com.example.newsapp.viewmodel.NewsListScreenViewModel

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListScreenViewModel = hiltViewModel()
) {
    val itemListState: LazyListState = rememberLazyListState()

    DisplayNewsItemList(newsList = viewModel.newsList, itemListState = itemListState, navController = navController)
}

@Composable
fun DisplayNewsItemList(
    newsList: List<News>,
    itemListState: LazyListState,
    navController: NavController
) {
    LazyColumn(
        state = itemListState
    ) {
        items(newsList) { item ->
            DisplayNewsItem(newsItem = item, navController = navController)
        }
    }
}

@Composable
fun DisplayNewsItem(newsItem: News, navController: NavController) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { navController.navigate(Routes.NEWS_DETAILS_SCREEN.id) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(4.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .weight(5F)
                    .fillMaxHeight()
                    .padding(4.dp),
                model = newsItem.imageUrl,
                contentDescription = newsItem.title,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(6F)
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                Text(
                    text = newsItem.title ?: "Title could not be loaded",
                    style = newsTitleListStyle
                )
                Text(
                    text = newsItem.publishDate ?: "Publish date could not be loaded",
                    style = newsDatePublishedStyle
                )
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
fun NewsListScreenPreview() {
    val navController = rememberNavController()
    NewsAppTheme {
        NewsListScreen(navController)
    }
}