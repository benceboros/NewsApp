package com.example.newsapp.util.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.example.newsapp.model.AnalyticsEvent
import com.example.newsapp.util.LocalAnalyticsHelper

fun AnalyticsHelper.logScreenView(screenName: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.EventTypes.SCREEN_VIEW,
            parameters = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.SCREEN_NAME, screenName),
            ),
        ),
    )
}

fun AnalyticsHelper.logItemSelect(itemId: String, itemName: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.EventTypes.SELECT_ITEM,
            parameters = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.ITEM_LIST_ID, itemId),
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.ITEM_LIST_NAME, itemName),
            ),
        ),
    )
}

fun AnalyticsHelper.logContentSelect(contentType: String, itemId: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.EventTypes.SELECT_CONTENT,
            parameters = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.CONTENT_TYPE, contentType),
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.ITEM_ID, itemId),
            ),
        ),
    )
}

fun AnalyticsHelper.logButtonClick(buttonId: String) {
    logEvent(
        AnalyticsEvent(
            type = AnalyticsEvent.EventTypes.BUTTON_CLICK,
            parameters = listOf(
                AnalyticsEvent.Param(AnalyticsEvent.EventParamKeys.BUTTON_ID, buttonId),
            ),
        ),
    )
}

/**
 * A side-effect which records a screen view event.
 */
@Composable
fun TrackScreenViewEvent(
    screenName: String,
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current,
) = DisposableEffect(Unit) {
    analyticsHelper.logScreenView(screenName)
    onDispose {}
}
