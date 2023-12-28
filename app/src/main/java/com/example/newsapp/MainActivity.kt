package com.example.newsapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.view.NewsDetailsScreen
import com.example.newsapp.view.NewsListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.NEWS_LIST_SCREEN.id) {
                        composable(Routes.NEWS_LIST_SCREEN.id) { NewsListScreen(navController) }
                        composable(Routes.NEWS_DETAILS_SCREEN.id) { NewsDetailsScreen(navController) }
                    }
                }
            }
        }
    }
}

enum class Routes(val id: String) {
    NEWS_LIST_SCREEN("NewsListScreen"),
    NEWS_DETAILS_SCREEN("NewsDetailsScreen")
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