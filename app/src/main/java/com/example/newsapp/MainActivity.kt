package com.example.newsapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.view.Navigation
import com.example.newsapp.view.screens.NewsListScreen
import com.example.newsapp.view.ScreenScaffoldWithMenu
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

        setContent {
            NewsAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    setupScreenTracking(navController)
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

    private fun setupScreenTracking(navController: NavHostController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val params = Bundle()
            params.putString(FirebaseAnalytics.Param.SCREEN_NAME, destination.route)
            params.putString(FirebaseAnalytics.Param.SCREEN_CLASS, destination.route)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
            Log.d("ScreenEvent", params.toString())
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