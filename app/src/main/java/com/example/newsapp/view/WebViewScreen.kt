package com.example.newsapp.view

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(
    url: String
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = {
            WebView(it).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }
    )
}