package com.example.chat.navigations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.ChatViewModel
import com.example.chat.screens.ChatScreen
import com.example.chat.screens.ProfileScreen
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


typealias ComposableFun = @Composable ()->Unit

sealed class TabItem(val title:String, val icons: ImageVector, val screens:ComposableFun) {

    object Chat : TabItem(title = "Chat",icons= Icons.Default.Chat, screens = { ChatScreen() })
    object Profile : TabItem(title = "Profile",icons= Icons.Default.AccountCircle, screens = { ProfileScreen() })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent() {

    val list = listOf(TabItem.Chat, TabItem.Profile)
    val pagerState = rememberPagerState(initialPage = 0)


    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(tabs = list, pagerState = pagerState)
        TabContent(tabs = list, pagerState = pagerState)
    }

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        indicator = { tabPositions ->
            Modifier.pagerTabIndicatorOffset(pagerState = pagerState, tabPositions = tabPositions)
        }) {
        tabs.forEachIndexed { index, tabItem ->

            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(tabItem.title) },
                icon = { Icon(imageVector = tabItem.icons, contentDescription = null) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                enabled = true
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(tabs:List<TabItem>,pagerState: PagerState) {
    HorizontalPager(count = tabs.size,state = pagerState) { page ->
        tabs[page].screens()
    }
}
