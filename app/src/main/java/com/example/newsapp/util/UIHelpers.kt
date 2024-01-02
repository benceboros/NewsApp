package com.example.newsapp.util

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.newsapp.di.AnalyticsModule.Companion.provideFirebaseAnalytics
import com.example.newsapp.util.analytics.AnalyticsHelper
import com.example.newsapp.util.analytics.FirebaseAnalyticsHelper

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    FirebaseAnalyticsHelper(provideFirebaseAnalytics())
}