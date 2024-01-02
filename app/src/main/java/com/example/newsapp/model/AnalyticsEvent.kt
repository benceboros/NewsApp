package com.example.newsapp.model

data class AnalyticsEvent(
    val type: String,
    val parameters: List<Param> = emptyList()
) {
    object EventTypes {
        const val SCREEN_VIEW = "screen_view"
        const val SELECT_ITEM = "select_item"
        const val SELECT_CONTENT = "select_content"
        const val BUTTON_CLICK = "button_click"
    }

    data class Param(val key: String, val value: String)

    object EventParamKeys {
        const val SCREEN_NAME = "screen_name"
        const val ITEM_LIST_ID = "item_list_id"
        const val ITEM_LIST_NAME = "item_list_name"
        const val CONTENT_TYPE = "content_type"
        const val ITEM_ID = "item_id"
        const val BUTTON_ID = "button_id"
    }
}
