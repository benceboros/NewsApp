package com.example.newsapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.view.InfoScreen
import com.example.newsapp.view.NewsDetailsScreen
import com.example.newsapp.view.NewsListScreen
import com.example.newsapp.view.ScreenScaffoldWithMenu
import com.example.newsapp.view.WebViewScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()
                    ScreenScaffoldWithMenu(
                        navController = navController,
                        scaffoldState = scaffoldState,
                        scaffoldContent = {
                            NavHost(navController = navController, startDestination = Routes.NEWS_LIST_SCREEN.id) {
                                composable(Routes.NEWS_LIST_SCREEN.id) { NewsListScreen(navController) }
                                composable(Routes.INFO_SCREEN.id) { InfoScreen(navController) }
                                composable(
                                    route = Routes.NEWS_DETAILS_SCREEN.id.plus("/{id}"),
                                    arguments = listOf(
                                        navArgument("id") {
                                            type = NavType.IntType
                                        }
                                    )
                                ) { navBackStackEntry ->
                                    navBackStackEntry.arguments?.getInt("id")?.let { newsId ->
                                        NewsDetailsScreen(
                                            navController = navController,
                                            newsId = newsId
                                        )
                                    }
                                }
                                composable(
                                    route = Routes.WEB_VIEW_SCREEN.id.plus("/{url}"),
                                    arguments = listOf(
                                        navArgument("url") {
                                            type = NavType.StringType
                                        }
                                    )
                                ) {navBackStackEntry ->
                                    navBackStackEntry.arguments?.getString("url")?.let { url ->
                                        WebViewScreen(urlToArticle = url)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

enum class Routes(val id: String) {
    NEWS_LIST_SCREEN("NewsListScreen"),
    NEWS_DETAILS_SCREEN("NewsDetailsScreen"),
    INFO_SCREEN("InfoScreen"),
    WEB_VIEW_SCREEN("WebViewScreen"),
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