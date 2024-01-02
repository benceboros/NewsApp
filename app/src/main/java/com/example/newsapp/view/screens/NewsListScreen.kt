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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.ui.theme.LightRed
import com.example.newsapp.ui.theme.LoadErrorDescriptionStyle
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.ui.theme.NoInternetBannerBtnStyle
import com.example.newsapp.ui.theme.NoInternetBannerDescStyle
import com.example.newsapp.ui.theme.UseApplicationOfflineStyle
import com.example.newsapp.ui.theme.newsDatePublishedStyle
import com.example.newsapp.ui.theme.newsTitleListStyle
import com.example.newsapp.util.Constants.UNKNOWN_ID_FOR_ANALYTICS
import com.example.newsapp.util.LocalAnalyticsHelper
import com.example.newsapp.util.analytics.AnalyticsHelper
import com.example.newsapp.util.analytics.TrackScreenViewEvent
import com.example.newsapp.util.analytics.logButtonClick
import com.example.newsapp.util.analytics.logContentSelect
import com.example.newsapp.util.analytics.logItemSelect
import com.example.newsapp.view.PageLoader
import com.example.newsapp.viewmodel.NewsListScreenViewModel

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListScreenViewModel = hiltViewModel()
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    val context = LocalContext.current

    val newsList by remember { viewModel.newsList }
    val isLoadingInitialNews by remember { viewModel.isLoadingInitialNews }
    val loadError by remember { viewModel.loadError }
    val paginationEndReached by remember { viewModel.paginationEndReached }
    val refreshing by remember { viewModel.refreshing }
    val noOfflineNews by remember { viewModel.noOfflineNews }

    val itemListState: LazyListState = rememberLazyListState()

    TrackScreenViewEvent(screenName = NavigationRoutes.NEWS_LIST_SCREEN.id)

    if (isLoadingInitialNews) {
        PageLoader()
    } else {
        if (newsList.isNotEmpty()) {
            DisplayNewsEntityList(
                itemListState = itemListState,
                navController = navController,
                newsList = newsList,
                loadError = loadError,
                paginationEndReached = paginationEndReached,
                refreshing = refreshing,
                loadNewsListPaginated = viewModel::loadNewsListPaginated,
                refreshNews = viewModel::refreshNews,
                loadedNewsItemCount = newsList.size,
                analyticsHelper = analyticsHelper,
                context = context
            )
        }
        if (loadError && newsList.isEmpty()) {
            DisplayNoInternetWithNoNews(
                loadNewsListPaginated = viewModel::loadNewsListPaginated,
                loadDbSavedNews = viewModel::loadDbSavedNews,
                analyticsHelper = analyticsHelper,
                context = context
            )
            if (noOfflineNews) {
                NoOfflineNewsDialog(
                    closeNoOfflineNewsDialog = viewModel::closeNoOfflineNewsDialog,
                    analyticsHelper = analyticsHelper
                )
            }
        }
    }
}

@Composable
fun NoOfflineNewsDialog(
    closeNoOfflineNewsDialog: () -> Unit,
    analyticsHelper: AnalyticsHelper
) {
    AlertDialog(
        containerColor = Color.White,
        shape = RectangleShape,
        onDismissRequest = {
            closeNoOfflineNewsDialog()
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
                    analyticsHelper.logButtonClick(
                        buttonId = "no_offline_news_ok_button"
                    )
                    closeNoOfflineNewsDialog()
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
    loadError: Boolean,
    paginationEndReached: Boolean,
    refreshing: Boolean,
    loadNewsListPaginated: () -> Unit,
    refreshNews: () -> Unit,
    loadedNewsItemCount: Int,
    analyticsHelper: AnalyticsHelper,
    context: Context
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = { refreshNews() })
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
    ) {
        Column {
            if (loadError) {
                NoInternetBanner(
                    refreshNews = {
                        refreshNews()
                    },
                    analyticsHelper = analyticsHelper
                )
            }
            LazyColumn(
                state = itemListState
            ) {
                items(loadedNewsItemCount) {
                    if (it >= loadedNewsItemCount - 1 && !paginationEndReached && !loadError) {
                        loadNewsListPaginated()
                    }
                    DisplayNewsEntity(
                        newsEntity = newsList[it],
                        navController = navController,
                        analyticsHelper = analyticsHelper,
                        context = context
                    )
                }
            }
        }
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun NoInternetBanner(
    refreshNews: () -> Unit,
    analyticsHelper: AnalyticsHelper
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
                            analyticsHelper.logButtonClick(
                                buttonId = "banner_try_again_button"
                            )
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
    loadDbSavedNews: () -> Unit,
    analyticsHelper: AnalyticsHelper,
    context: Context
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
                style = LoadErrorDescriptionStyle
            )

            Button(
                onClick = {
                    analyticsHelper.logButtonClick(
                        buttonId = "retry_button"
                    )
                    loadNewsListPaginated()
                },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .semantics {
                        contentDescription = context.getString(R.string.content_desc_btn_retry)
                    }
            ) {
                Text(
                    text = stringResource(R.string.btn_retry).uppercase(),
                )
            }

            Text(
                modifier = Modifier
                    .clickable {
                        analyticsHelper.logButtonClick(
                            buttonId = "use_app_offline_button"
                        )
                        loadDbSavedNews()
                    }
                    .semantics {
                        contentDescription = context.getString(R.string.content_desc_use_offline)
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
    navController: NavController,
    analyticsHelper: AnalyticsHelper,
    context: Context
) {
    var imageIsLoading by remember { mutableStateOf(false) }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                analyticsHelper.logItemSelect(
                    itemId = newsEntity.id?.toString() ?: UNKNOWN_ID_FOR_ANALYTICS,
                    itemName = newsEntity.title!!.toString()
                )
                analyticsHelper.logContentSelect(
                    contentType = "news article",
                    itemId = newsEntity.id?.toString() ?: UNKNOWN_ID_FOR_ANALYTICS
                )
                navController.navigate("${NavigationRoutes.NEWS_DETAILS_SCREEN.id}/${newsEntity.id}")
            }
            .semantics {
                contentDescription = context.getString(R.string.content_desc_clickable_news_article)
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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(5F)
                    .semantics {
                        contentDescription = context.getString(R.string.content_desc_image_container)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageIsLoading) {
                    CircularProgressIndicator()
                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(4.dp),
                    model = newsEntity.imageUrl,
                    contentDescription = stringResource(R.string.content_desc_image_of_news_article),
                    onLoading = { imageIsLoading = true },
                    onSuccess = { imageIsLoading = false },
                    onError = { imageIsLoading = false },
                    error = painterResource(id = R.drawable.ic_broken_image)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(6F)
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                Text(
                    text = newsEntity.title ?: stringResource(id = R.string.unknown_title),
                    style = newsTitleListStyle,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .semantics {
                            contentDescription = context.getString(R.string.content_desc_news_article_title)
                        }
                )
                Text(
                    text = newsEntity.publishDate ?: stringResource(id = R.string.unknown_publish_date),
                    style = newsDatePublishedStyle,
                    modifier = Modifier
                        .semantics {
                            contentDescription = context.getString(R.string.content_desc_news_article_publish_date)
                        }
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