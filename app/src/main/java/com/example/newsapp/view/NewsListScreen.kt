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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.Routes
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.ui.theme.LightRed
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.NoInternetBannerBtnStyle
import com.example.newsapp.ui.theme.NoInternetBannerDescStyle
import com.example.newsapp.ui.theme.UseApplicationOfflineStyle
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
    val noOfflineNews = remember { viewModel.noOfflineNews }

    val loadedNewsItemCount = newsList.size
    val itemListState: LazyListState = rememberLazyListState()

    if (isLoadingInitialNews) {
        PageLoader()
    } else {
        if (loadedNewsItemCount > 0) {
            DisplayNewsEntityList(
                itemListState = itemListState,
                navController = navController,
                newsList = newsList,
                loadErrorState = viewModel.loadError,
                paginationEndReachedState = viewModel.paginationEndReached,
                refreshingState = viewModel.refreshing,
                loadNewsListPaginated = viewModel::loadNewsListPaginated,
                refreshNews = viewModel::refreshNews,
                loadedNewsItemCount = loadedNewsItemCount
            )
        }
        if (viewModel.loadError.value && loadedNewsItemCount < 1) {
            DisplayNoInternetWithNoNews(
                loadNewsListPaginated = viewModel::loadNewsListPaginated,
                loadDbSavedNews = viewModel::loadDbSavedNews
            )
            if (noOfflineNews.value) {
                NoOfflineNewsDialog(noOfflineNews = noOfflineNews)
            }
        }
    }
}

@Composable
fun NoOfflineNewsDialog(
    noOfflineNews: MutableState<Boolean>
) {
    AlertDialog(
        containerColor = Color.White,
        shape = RectangleShape,
        onDismissRequest = {
            noOfflineNews.value = false
        },
        title = {
            Text(text = stringResource(R.string.title_could_not_load_offline_news))
        },
        text = {
            Text(stringResource(R.string.desc_could_not_load_offline_news))
        },
        confirmButton = {
            Button(
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                onClick = {
                    noOfflineNews.value = false
                }
            ) {
                Text(stringResource(R.string.btn_ok))
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayNewsEntityList(
    itemListState: LazyListState,
    navController: NavController,
    newsList: List<NewsEntity>,
    loadErrorState: MutableState<Boolean>,
    paginationEndReachedState: MutableState<Boolean>,
    refreshingState: MutableState<Boolean>,
    loadNewsListPaginated: () -> Unit,
    refreshNews: () -> Unit,
    loadedNewsItemCount: Int,
) {
    val loadError by remember { loadErrorState }
    val paginationEndReached by remember { paginationEndReachedState }

    val pullRefreshState = rememberPullRefreshState(refreshing = refreshingState.value, onRefresh = { refreshNews() })
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Column {
            if (loadError) {
                NoInternetBanner(
                    refreshNews = {
                        refreshNews()
                    }
                )
            }
            LazyColumn(
                state = itemListState
            ) {
                items(loadedNewsItemCount) {
                    if (it >= loadedNewsItemCount - 1 && !paginationEndReached && !loadError) {
                        loadNewsListPaginated()
                    }
                    DisplayNewsEntity(newsEntity = newsList[it], navController = navController)
                }
            }
        }
        PullRefreshIndicator(refreshingState.value, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun NoInternetBanner(
    refreshNews: () -> Unit
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
                    text = stringResource(R.string.news_could_not_be_updated),
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
                            refreshNews()
                        }
                )
            }
        }
    }
}

@Composable
fun DisplayNoInternetWithNoNews(
    loadNewsListPaginated: () -> Unit,
    loadDbSavedNews: () -> Unit
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
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_retry).uppercase(),
                )
            }

            Text(
                modifier = Modifier
                    .clickable {
                        loadDbSavedNews()
                    },
                text = stringResource(R.string.use_offline),
                style = UseApplicationOfflineStyle
            )
        }
    }
}

@Composable
fun DisplayNewsEntity(
    newsEntity: NewsEntity,
    navController: NavController
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                navController.navigate("${Routes.NEWS_DETAILS_SCREEN.id}/${newsEntity.id}")
            },
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
                model = newsEntity.imageUrl,
                contentDescription = newsEntity.title,
                error = painterResource(id = R.drawable.ic_broken_image)
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
                    text = newsEntity.title ?: "Title could not be loaded",
                    style = newsTitleListStyle,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = newsEntity.publishDate ?: "Publish date could not be loaded",
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