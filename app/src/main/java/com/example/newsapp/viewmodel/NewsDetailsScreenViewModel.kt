package com.example.newsapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.data.local.entities.NewsEntity
import com.example.newsapp.model.data.local.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    var state by mutableStateOf(NewsDetailsScreenState())
        private set

    fun onEvent(event: NewsDetailsScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is NewsDetailsScreenEvent.NewsItemSelected -> {
                    if (event.id != null) {
                        state = state.copy(
                            selectedNewsEntity = newsRepository.getSelectedNewsEntity(event.id)
                        )
                    }
                }
            }
        }
    }
}

data class NewsDetailsScreenState(
    val selectedNewsEntity: NewsEntity? = null
)

sealed class NewsDetailsScreenEvent {
    data class NewsItemSelected(val id: Int?) : NewsDetailsScreenEvent()
}