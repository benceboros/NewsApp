package com.example.newsapp.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.TechnologyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(

) : ViewModel() {
    val technologies: List<TechnologyItem> = listOf(
        TechnologyItem(
            name = "MVVM Architecture",
            websiteUrl = "https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel"
        ),
        TechnologyItem(
            name = "Jetpack Compose",
            websiteUrl = "https://developer.android.com/jetpack/compose"
        ),
        TechnologyItem(
            name = "Hilt - Dependency Injection",
            websiteUrl = "https://developer.android.com/training/dependency-injection/hilt-android"
        ),
        TechnologyItem(
            name = "Coil",
            websiteUrl = "https://github.com/coil-kt/coil"
        ),
        TechnologyItem(
            name = "Material Design",
            websiteUrl = "https://m3.material.io/"
        ),
        TechnologyItem(
            name = "Coroutines",
            websiteUrl = "https://kotlinlang.org/docs/coroutines-overview.html"
        )
    )

    val contactInformation: List<ContactInformation> = listOf(
        ContactInformation(
            name = "Phone",
            value = "+45 50 33 44 81"
        ),
        ContactInformation(
            name = "E-mail",
            value = "benceboros140@gmail.com"
        ),
        ContactInformation(
            name = "LinkedIn",
            value = "https://www.linkedin.com/in/benceboros"
        )
    )

    fun openDialer(context: Context, phoneNumber: String) {
        val phoneUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_DIAL, phoneUri)
        try {
            context.startActivity(dialIntent)
        } catch (s: SecurityException) {
            Toast.makeText(context, "Unable to open Phone app's dialer", Toast.LENGTH_LONG).show()
        }
    }

    fun openEmailApp(context: Context, emailAddress: String) {
        try {
            val emailUri = Uri.parse("mailto:$emailAddress")
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.setData(emailUri)
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No email app is available", Toast.LENGTH_LONG).show()
        } catch (t: Throwable) {
            Toast.makeText(context, "Unable to open phone's email app", Toast.LENGTH_LONG).show()
        }
    }
}