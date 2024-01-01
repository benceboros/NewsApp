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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.newsapp.R
import com.example.newsapp.Routes
import com.example.newsapp.model.MenuItem
import com.example.newsapp.ui.theme.topAppBarTitleStyle
import com.example.newsapp.view.screens.InfoScreen
import com.example.newsapp.view.screens.NewsDetailsScreen
import com.example.newsapp.view.screens.NewsListScreen
import com.example.newsapp.view.screens.WebViewScreen
import kotlinx.coroutines.launch


@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.NEWS_LIST_SCREEN.id) {
        composable(Routes.NEWS_LIST_SCREEN.id) { NewsListScreen(navController) }
        composable(Routes.INFO_SCREEN.id) { InfoScreen(navController) }
        composable(
            route = Routes.NEWS_DETAILS_SCREEN.id.plus("/{id}"),
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getInt("id")?.let { newsId ->
                NewsDetailsScreen(
                    navController = navController,
                    newsId = newsId
                )
            }
        }
        composable(
            route = Routes.WEB_VIEW_SCREEN.id.plus("/{url}"),
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) {navBackStackEntry ->
            navBackStackEntry.arguments?.getString("url")?.let { url ->
                WebViewScreen(urlToArticle = url)
            }
        }
    }
}

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
                navController = navController,
                onMenuIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onBackIconClick = {
                    navController.popBackStack()
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
                        title = stringResource(R.string.menu_item_news_list),
                        contentDescription = stringResource(R.string.content_desc_go_to_news_list_screen),
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = Routes.INFO_SCREEN.id,
                        title = stringResource(R.string.menu_item_info),
                        contentDescription = stringResource(R.string.content_desc_go_to_information_screen),
                        icon = Icons.Default.Info
                    )
                ),
                onItemClick = {
                    if (navController.currentBackStackEntry?.destination?.route == Routes.NEWS_LIST_SCREEN.id) {
                        if (it.id == Routes.INFO_SCREEN.id) {
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
    navController: NavController,
    onMenuIconClick: () -> Unit,
    onBackIconClick: () -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = topAppBarTitleStyle
            )
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIcon = {
            when (currentBackStackEntry?.destination?.route) {
                Routes.NEWS_LIST_SCREEN.id, Routes.INFO_SCREEN.id -> {
                    IconButton(onClick = onMenuIconClick) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.content_desc_toggle_menu)
                        )
                    }
                }
                else -> {
                    IconButton(onClick = onBackIconClick) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_go_back_to_previous_screen)
                        )
                    }
                }
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
            text = stringResource(R.string.title_navigation_drawer_header),
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