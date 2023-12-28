package com.example.newsapp.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.model.ContactInformation
import com.example.newsapp.model.TechnologyItem
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.viewmodel.InfoScreenViewModel

@Composable
fun InfoScreen(
    viewModel: InfoScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoHeader(modifier = Modifier.padding(bottom = 16.dp), headerTitle = "INFORMATION")
        Text(
            text = "This is a News application created by Bence Boros. It uses News API, a free public REST API to obtain the articles.",
            fontSize = 15.sp,
            modifier = Modifier
                .padding(start = 8.dp, bottom = 16.dp)
        )
        InfoHeader(headerTitle = "TECHNOLOGIES USED")
        TechnologyList(viewModel.technologies)
        InfoHeader(headerTitle = "CONTACT")
        ContactInformation(viewModel.contactInformation)
    }
}

@Composable
fun InfoHeader(
    modifier: Modifier = Modifier,
    headerTitle: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = headerTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TechnologyList(
    technologyList: List<TechnologyItem>
) {
    Column {
        technologyList.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { println("$item clicked") },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    fontSize = 18.sp
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Go to URL icon"
                )
            }
            if (item != technologyList.last()) {
                Divider(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ContactInformation(
    contactInfoItems: List<ContactInformation>
) {
    Column {
        contactInfoItems.forEach { item ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { println("$item clicked") },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.name}:",
                    fontSize = 18.sp
                )
                Text(
                    text = item.value,
                    fontSize = if (item.name == "LinkedIn") 13.sp else 16.sp
                )
            }
            if (item != contactInfoItems.last()) {
                Divider(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Composable
fun InfoScreenPreview() {
    NewsAppTheme {
        InfoScreen()
    }
}