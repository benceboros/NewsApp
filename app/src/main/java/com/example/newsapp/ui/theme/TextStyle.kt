package com.example.newsapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

val newsTitleListStyle = TextStyle(
    fontSize = 15.sp,
    fontWeight = FontWeight.Bold
)

val newsTitleDetailStyle = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    color = Color.Gray
)

val newsDescriptionStyle = TextStyle(
    fontSize = 15.sp,
    color = Color.Gray
)

val newsDatePublishedStyle = TextStyle(
    fontSize = 12.sp,
    color = Color.Gray
)

val newsDateAndAuthorStyle = TextStyle(
    fontSize = 13.sp,
    color = Color.Gray,
    textAlign = TextAlign.End
)

val contactInfoHeaderStyle = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color.White
)

val contactInfoStyle = TextStyle(
    textDecoration = TextDecoration.Underline
)

val topAppBarTitleStyle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color.White
)

val NoInternetBannerDescStyle = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.SemiBold
)

val NoInternetBannerBtnStyle = TextStyle(
    fontSize = 15.sp,
    color = DarkRed,
    fontWeight = FontWeight.SemiBold,
)

val UseApplicationOfflineStyle = TextStyle(
    color = DarkRed,
    textDecoration = TextDecoration.Underline
)

val LoadErrorDescriptionStyle = TextStyle(
    textAlign = TextAlign.Center
)