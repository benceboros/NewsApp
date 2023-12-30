package com.example.newsapp.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.Routes
import com.example.newsapp.model.NewsItem
import com.example.newsapp.ui.theme.LightRed
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.NoInternetBannerBtnStyle
import com.example.newsapp.ui.theme.NoInternetBannerDescStyle
import com.example.newsapp.ui.theme.newsDatePublishedStyle
import com.example.newsapp.ui.theme.newsTitleListStyle
import com.example.newsapp.viewmodel.NewsListScreenViewModel

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListScreenViewModel = hiltViewModel()
) {
    val newsList by remember { viewModel.newsList }
    val isLoadingInitialNews by remember { viewModel.isLoadingInitialNews }

    val loadedNewsItemCount = newsList.size

    val itemListState: LazyListState = rememberLazyListState()

    if (isLoadingInitialNews) {
        PageLoader()
    } else {
        if (loadedNewsItemCount > 0) {
            DisplayNewsItemList(
                itemListState = itemListState,
                navController = navController,
                newsList = newsList,
                loadErrorMessageState = viewModel.loadErrorMessage,
                paginationEndReachedState = viewModel.paginationEndReached,
                loadNewsListPaginated = viewModel::loadNewsListPaginated,
                loadedNewsItemCount = loadedNewsItemCount
            )
        } else {
            DisplayNoInternetWithNoNews(
                loadNewsListPaginated = viewModel::loadNewsListPaginated
            )
        }
    }
}

@Composable
fun DisplayNewsItemList(
    itemListState: LazyListState,
    navController: NavController,
    newsList: List<NewsItem>,
    loadErrorMessageState: MutableState<String>,
    paginationEndReachedState: MutableState<Boolean>,
    loadNewsListPaginated: () -> Unit,
    loadedNewsItemCount: Int,
) {
    var loadErrorMessage by remember { loadErrorMessageState }
    val paginationEndReached by remember { paginationEndReachedState }

    Column {
        if (loadErrorMessage.isNotBlank()) {
            NoInternetBanner(
                loadNewsListPaginated = {
                    loadErrorMessage = ""
                    loadNewsListPaginated()
                }
            )
        }
        LazyColumn(
            state = itemListState
        ) {
            items(loadedNewsItemCount) {
                if (it >= loadedNewsItemCount - 1 && !paginationEndReached) {
                    loadNewsListPaginated()
                }
                DisplayNewsItem(newsItem = newsList[it], navController = navController)
            }
        }
    }
}

@Composable
fun NoInternetBanner(
    loadNewsListPaginated: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = LightRed
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(5F)
            ) {
                Text(
                    text = stringResource(R.string.further_news_could_not_be_loaded),
                    style = NoInternetBannerDescStyle
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(2F)
            ) {
                Text(
                    text = stringResource(R.string.btn_try_again),
                    style = NoInternetBannerBtnStyle,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            loadNewsListPaginated()
                        }
                )
            }
        }
    }
}

@Composable
fun DisplayNoInternetWithNoNews(
    loadNewsListPaginated: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painterResource(id = R.drawable.ic_no_network),
                tint = Color.LightGray,
                contentDescription = stringResource(R.string.content_desc_no_network_icon)
            )
            Text(
                text = stringResource(R.string.news_could_not_be_loaded),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    loadNewsListPaginated()
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_retry).uppercase(),
                )
            }
        }
    }
}

@Composable
fun DisplayNewsItem(newsItem: NewsItem, navController: NavController) {
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

@Composable
fun PageLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp, 64.dp)
        )
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