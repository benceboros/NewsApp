package com.example.newsapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.R
import com.example.newsapp.Routes
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScreenScaffoldWithMenu(
    navController: NavController,
    scaffoldState: ScaffoldState,
    scaffoldContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerShape = menuOutlineShape(0.dp, 0.7f),
        topBar = {
            AppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            NavigationDrawerHeader()
            NavigationDrawerBody(
                items = listOf(
                    MenuItem(
                        id = Routes.NEWS_LIST_SCREEN.id,
                        title = "News List",
                        contentDescription = "Go to news list screen",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = Routes.NEWS_DETAILS_SCREEN.id,
                        title = "Info",
                        contentDescription = "Go to information screen",
                        icon = Icons.Default.Info
                    )
                ),
                onItemClick = {
                    if (navController.currentBackStackEntry?.destination?.route == Routes.NEWS_LIST_SCREEN.id) {
                        if (it.id == Routes.NEWS_DETAILS_SCREEN.id) {
                            navController.navigate(it.id)
                        }
                    } else {
                        if (it.id == Routes.NEWS_LIST_SCREEN.id) {
                            navController.popBackStack()
                        }
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        content = {
            scaffoldContent()
        }
    )
}

@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle Menu"
                )
            }
        }
    )
}

@Composable
fun NavigationDrawerHeader() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp),
    ) {
        Text(
            text = "News App",
            fontSize = 32.sp
        )
    }
}

@Composable
fun NavigationDrawerBody(
    items: List<MenuItem>,
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )
                Text(
                    text = item.title,
                    modifier = Modifier
                        .weight(1F)
                )
            }
        }
    }
}

/**
 * @param widthOffset Specifies the exact width of the Navigation Drawer Menu in Dp
 * @param scale Specifies the width of the Navigation Menu relative to the screen width in Float (e.g. 0.5f means half of the screen)
 * @return The Outline Shape of the Navigation Drawer Menu with custom width
 */
fun menuOutlineShape(widthOffset: Dp, scale: Float) = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Offset(
                    size.width * scale + with(density) { widthOffset.toPx() },
                    size.height
                )
            )
        )
    }
}