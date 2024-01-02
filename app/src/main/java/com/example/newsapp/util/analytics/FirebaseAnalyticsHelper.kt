package com.example.newsapp.util.analytics

import android.util.Log
import com.example.newsapp.model.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d("AnalyticsEventBeingLogged!", "$event")
        firebaseAnalytics.logEvent(event.type) {
            for (extra in event.parameters) {
                // Limit parameter keys and values according to firebase maximum length values.
                param(
                    key = extra.key.take(40),
                    value = extra.value.take(100),
                )
            }
        }
    }
}