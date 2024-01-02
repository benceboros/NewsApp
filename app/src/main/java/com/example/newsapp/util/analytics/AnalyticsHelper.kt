package com.example.newsapp.util.analytics

import com.example.newsapp.model.AnalyticsEvent

interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}