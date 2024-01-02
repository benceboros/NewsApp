package com.example.newsapp.view.screens

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.R
import com.example.newsapp.ui.theme.LoadErrorDescriptionStyle
import com.example.newsapp.util.analytics.TrackScreenViewEvent
import com.example.newsapp.view.PageLoader
import com.example.newsapp.view.Routes
import com.example.newsapp.viewmodel.WebViewScreenEvent
import com.example.newsapp.viewmodel.WebViewScreenViewModel

@Composable
fun WebViewScreen(
    urlToArticle: String,
    viewModel: WebViewScreenViewModel = hiltViewModel()
) {
    TrackScreenViewEvent(screenName = Routes.WEB_VIEW_SCREEN.id)

    if (viewModel.state.isLoading) {
        PageLoader()
    }
    if (!viewModel.state.loadError) {
        WebViewPage(
            urlToArticle = urlToArticle,
            isLoading = viewModel.state.isLoading,
            loadError = viewModel.state.loadError,
            onWebViewScreenEvent = viewModel::onEvent
        )
    }
    if (viewModel.state.loadError) {
        ErrorLoadingArticlePage()
    }
}

@Composable
fun WebViewPage(
    urlToArticle: String,
    isLoading: Boolean,
    loadError: Boolean,
    onWebViewScreenEvent: (webViewScreenEvent: WebViewScreenEvent) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = {
            WebView(it).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadsImagesAutomatically = true
                    allowFileAccess = true
                    allowContentAccess = true
                }
                webViewClient = object : WebViewClient() {
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        if ((error?.description == "net::ERR_NAME_NOT_RESOLVED") || error?.description == "net::ERR_INTERNET_DISCONNECTED") {
                            onWebViewScreenEvent(WebViewScreenEvent.OnLoadingError)
                        }
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onWebViewScreenEvent(WebViewScreenEvent.OnLoadingInProgress(isLoading = true))
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onWebViewScreenEvent(WebViewScreenEvent.OnLoadingInProgress(isLoading = false))
                    }
                }
                loadUrl(urlToArticle)
            }
        }
    ) {
        it.visibility = if (isLoading || loadError) View.GONE else View.VISIBLE
    }
}

@Composable
fun ErrorLoadingArticlePage() {
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
                painter = painterResource(id = R.drawable.ic_webpage_error),
                tint = Color.Gray,
                contentDescription = stringResource(R.string.content_desc_ic_webpage_error)
            )
            Text(
                text = stringResource(R.string.webpage_could_not_be_loaded),
                style = LoadErrorDescriptionStyle
            )
        }
    }
}
