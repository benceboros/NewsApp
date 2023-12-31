package com.example.newsapp.viewmodel

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.newsapp.R
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.ContactInformationType
import com.example.newsapp.model.TechnologyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(

) : ViewModel() {
    fun getTechnologies(context: Context): List<TechnologyItem> = listOf(
        TechnologyItem(
            name = context.getString(R.string.mvvm_architecture),
            websiteUrl = context.getString(R.string.url_mvvm_architecture)
        ),
        TechnologyItem(
            name = context.getString(R.string.jetpack_compose),
            websiteUrl = context.getString(R.string.url_jetpack_compose)
        ),
        TechnologyItem(
            name = context.getString(R.string.hilt_dependency_injection),
            websiteUrl = context.getString(R.string.url_hilt)
        ),
        TechnologyItem(
            name = context.getString(R.string.coil),
            websiteUrl = context.getString(R.string.url_coil)
        ),
        TechnologyItem(
            name = context.getString(R.string.material_design),
            websiteUrl = context.getString(R.string.url_material_design)
        ),
        TechnologyItem(
            name = context.getString(R.string.coroutines),
            websiteUrl = context.getString(R.string.url_coroutines)
        ),
        TechnologyItem(
            name = context.getString(R.string.retrofit),
            websiteUrl = context.getString(R.string.url_retrofit)
        ),
        TechnologyItem(
            name = context.getString(R.string.moshi),
            websiteUrl = context.getString(R.string.url_moshi)
        ),
        TechnologyItem(
            name = context.getString(R.string.room),
            websiteUrl = context.getString(R.string.url_room)
        ),
        TechnologyItem(
            name = context.getString(R.string.mockk),
            websiteUrl = context.getString(R.string.url_mockk)
        ),
        TechnologyItem(
            name = context.getString(R.string.firebase_crashlytics),
            websiteUrl = context.getString(R.string.url_firebase_crashlytics)
        ),
        TechnologyItem(
            name = context.getString(R.string.google_analytics),
            websiteUrl = context.getString(R.string.url_google_analytics)
        )
    )

    fun getContactInformation(context: Context): List<ContactInformation> = listOf(
        ContactInformation(
            type = ContactInformationType.PHONE,
            typeName = context.getString(R.string.phone),
            value = context.getString(R.string.phone_value)
        ),
        ContactInformation(
            type = ContactInformationType.EMAIL,
            typeName = context.getString(R.string.e_mail),
            value = context.getString(R.string.e_mail_value)
        ),
        ContactInformation(
            type = ContactInformationType.LINKEDIN,
            typeName = context.getString(R.string.linkedin),
            value = context.getString(R.string.linkedin_value)
        )
    )

    /**
     * Open the given phone number in the device's Phone app
     *
     * @param context Context for starting activity for ACTION_DIAL intent
     * @param phoneNumber The phone number to be dialed in String format
     */
    fun openDialer(context: Context, phoneNumber: String) {
        val phoneUri = Uri.parse("tel:$phoneNumber")
        val dialIntent = Intent(Intent.ACTION_DIAL, phoneUri)
        try {
            context.startActivity(dialIntent)
        } catch (s: SecurityException) {
            Toast.makeText(context, context.getString(R.string.error_msg_unable_to_open_phone_dialer), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Gives the option to send an email to the given email address in an external email application
     *
     * @param context Context for starting activity for ACTION_SENDTO intent
     * @param emailAddress The email address in String format
     */
    fun openEmailApp(context: Context, emailAddress: String) {
        try {
            val emailUri = Uri.parse("mailto:$emailAddress")
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.setData(emailUri)
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.error_msg_no_email_app_available), Toast.LENGTH_SHORT).show()
        } catch (t: Throwable) {
            Toast.makeText(context, context.getString(R.string.error_msg_unable_to_open_email_app), Toast.LENGTH_SHORT).show()
        }
    }
}