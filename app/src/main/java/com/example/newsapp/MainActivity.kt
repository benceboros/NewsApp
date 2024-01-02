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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.util.LocalAnalyticsHelper
import com.example.newsapp.util.analytics.AnalyticsHelper
import com.example.newsapp.view.Navigation
import com.example.newsapp.view.screens.NewsListScreen
import com.example.newsapp.view.ScreenScaffoldWithMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper
            ) {
                NewsAppTheme {
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        val navController = rememberNavController()
                        val scaffoldState = rememberScaffoldState()
                        ScreenScaffoldWithMenu(
                            navController = navController,
                            scaffoldState = scaffoldState,
                            scaffoldContent = {
                                Navigation(navController = navController)
                            }
                        )
                    }
                }
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