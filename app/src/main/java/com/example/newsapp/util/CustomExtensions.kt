package com.example.newsapp.util

import android.widget.Toast
import androidx.navigation.NavController
import com.example.newsapp.R
import com.example.newsapp.Routes
import com.example.newsapp.model.data.remote.respones.Article
import java.lang.Exception
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Article.canBeSaved(): Boolean =
    urlToImage != null && title != null && publishedAt != null

fun String.formatToAppropriateDateFormat(): String {
    val date = ZonedDateTime.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm")
    return date.format(formatter)
}

fun NavController.navigateToUrl(url: String?) {
    if (url != null) {
        try {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            navigate("${Routes.WEB_VIEW_SCREEN.id}/$encodedUrl")
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.error_msg_failed_to_open_url), Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, context.getString(R.string.error_msg_unable_to_open_url), Toast.LENGTH_SHORT).show()
    }
}
