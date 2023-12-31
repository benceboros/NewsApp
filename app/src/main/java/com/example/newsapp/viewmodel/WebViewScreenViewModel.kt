package com.example.newsapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewScreenViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(WebViewScreenState())
        private set

    fun onEvent(event: WebViewScreenEvent) {
        viewModelScope.launch {
            state = when (event) {
                WebViewScreenEvent.OnLoadingError -> {
                    state.copy(
                        isLoading = false,
                        loadError = true
                    )
                }

                is WebViewScreenEvent.OnLoadingInProgress -> {
                    state.copy(
                        isLoading = event.isLoading
                    )
                }
            }
        }
    }
}

data class WebViewScreenState(
    val isLoading: Boolean = true,
    val loadError: Boolean = false
)

sealed class WebViewScreenEvent {
    object OnLoadingError : WebViewScreenEvent()
    data class OnLoadingInProgress(val isLoading: Boolean) : WebViewScreenEvent()
}