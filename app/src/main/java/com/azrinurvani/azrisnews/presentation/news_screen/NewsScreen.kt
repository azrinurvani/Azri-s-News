package com.azrinurvani.azrisnews.presentation.news_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.azrinurvani.azrisnews.domain.model.Article
import com.azrinurvani.azrisnews.presentation.component.CategoryTabRow
import com.azrinurvani.azrisnews.presentation.component.NewsArticleCard
import com.azrinurvani.azrisnews.presentation.component.NewsScreenTopBar
import com.azrinurvani.azrisnews.presentation.component.RetryContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    state: NewsScreenState,
    onEvent : (NewsScreenEvent) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val categories = listOf(
        "General",
        "Business",
        "Health",
        "Science",
        "Sports",
        "Technology",
        "Entertainment"
    )
//    val pagerState = rememberPagerState(initialPage = 0, pageCount = { categories.size })
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { categories.size }
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect{ page->
            onEvent(NewsScreenEvent.OnCategoryChange(category = categories[page]))
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NewsScreenTopBar(
                scrollBehavior = scrollBehavior,
                onSearchIconClicked = {}
            )
        }
    ) { innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            CategoryTabRow(
                pagerState = pagerState,
                categories = categories,
                onTabSelected = { index ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
            HorizontalPager(
                state = pagerState
            ) {
                NewsArticleList(
                    state = state,
                    onCardClicked = {

                    },
                    onRetry = {
                        onEvent(NewsScreenEvent.OnCategoryChange(state.category))
                    }
                )
            }
        }

    }
}

@Composable
fun NewsArticleList(
    state: NewsScreenState,
    onCardClicked: (Article) -> Unit,
    onRetry : () -> Unit
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(state.articles){ article->
            NewsArticleCard(
                article = article,
                onCardClick = {  }
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        if (state.error != null){
            RetryContent(
                error = state.error,
                onRetry = onRetry
            )
        }
    }
}

//state: Any value that can change during the usage of app
//event: All the possible actions user can do