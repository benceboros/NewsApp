package com.example.newsapp.view

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.newsapp.viewmodel.NewsDetailsScreenViewModel

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    viewModel: NewsDetailsScreenViewModel = hiltViewModel()
) {
    Button(onClick = {
        navController.popBackStack()
    }) {
        Text(text = "News Details Screen")
    }
}