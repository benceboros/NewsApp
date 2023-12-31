package com.example.newsapp.util

import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.example.newsapp.R
import com.example.newsapp.model.NavigationRoutes
import com.example.newsapp.model.data.remote.respones.Article
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.Exception

fun Article.canBeSaved(): Boolean =
    urlToImage != null && title != null && publishedAt != null

fun String.formatToAppropriateDateFormat(): String? {
    return try {
        val date = ZonedDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm")
        date.format(formatter)
    } catch (e: Exception) {
        Log.e("DateFormatException", "Reason: $e")
        null
    }
}

fun NavController.navigateToUrl(url: String?) {
    if (url != null) {
        try {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            navigate("${NavigationRoutes.WEB_VIEW_SCREEN.id}/$encodedUrl")
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.error_msg_failed_to_open_url), Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, context.getString(R.string.error_msg_unable_to_open_url), Toast.LENGTH_SHORT).show()
    }
}
