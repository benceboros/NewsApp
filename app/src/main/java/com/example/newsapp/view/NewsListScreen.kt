package com.example.newsapp.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsapp.Routes
import com.example.newsapp.viewmodel.NewsListScreenViewModel

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListScreenViewModel = hiltViewModel()
) {
    Button(onClick = { navController.navigate(Routes.NEWS_DETAILS_SCREEN.id) }) {
        Text(text = "News List Screen")
    }
}