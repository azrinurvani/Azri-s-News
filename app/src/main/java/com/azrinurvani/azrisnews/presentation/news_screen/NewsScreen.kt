package com.azrinurvani.azrisnews.presentation.news_screen

import androidx.compose.animation.Crossfade
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.azrinurvani.azrisnews.domain.model.Article
import com.azrinurvani.azrisnews.presentation.component.BottomSheetContent
import com.azrinurvani.azrisnews.presentation.component.CategoryTabRow
import com.azrinurvani.azrisnews.presentation.component.NewsArticleCard
import com.azrinurvani.azrisnews.presentation.component.NewsScreenTopBar
import com.azrinurvani.azrisnews.presentation.component.RetryContent
import com.azrinurvani.azrisnews.presentation.component.SearchAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    state: NewsScreenState,
    onEvent : (NewsScreenEvent) -> Unit,
    onReadFullStoryReadButtonClicked: (String) -> Unit
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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) //skipPartiallyExpanded its mean for full and half page to show
    var shouldBottomSheetShow by remember { mutableStateOf(false) }

    val focusRequester = remember{ FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    if (shouldBottomSheetShow){
        ModalBottomSheet(
            onDismissRequest = {
                shouldBottomSheetShow = false
            },
            sheetState = sheetState,
            content = {
                state.selectedArticle?.let {
                    BottomSheetContent(
                        article = it,
                        onReadFullStoryButtonClicked = {
                            onReadFullStoryReadButtonClicked(it.url)
                            coroutineScope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) shouldBottomSheetShow = false
                            }
                        }
                    )
                }
            }
        )
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect{ page->
            onEvent(NewsScreenEvent.OnCategoryChange(category = categories[page]))
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Crossfade(
            targetState = state.isSearchBarVisible
        ) { isVisible ->
            if (isVisible) {
                Column {
                    SearchAppBar(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = state.searchQuery,
                        onInputValueChange = { newValue ->
                            onEvent(NewsScreenEvent.OnSearchQueryChange(newValue))
                        },
                        onCloseIconClicked = {
                            onEvent(NewsScreenEvent.OnCloseIconClicked)
                        },
                        onSearchIconClicked = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                    NewsArticleList(
                        state = state,
                        onCardClicked = { article ->
                            shouldBottomSheetShow = true
                            onEvent(NewsScreenEvent.OnNewsCardClicked(article = article))
                        },
                        onRetry = {
                            onEvent(NewsScreenEvent.OnCategoryChange(state.category))
                        }
                    )
                }
            }else{
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        NewsScreenTopBar(
                            scrollBehavior = scrollBehavior,
                            onSearchIconClicked = {
                                coroutineScope.launch {
                                    delay(500)
                                    focusRequester.requestFocus()
                                }
                                onEvent(NewsScreenEvent.OnSearchIconClicked)
                            }
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
                                onCardClicked = { article ->
                                    shouldBottomSheetShow = true
                                    onEvent(NewsScreenEvent.OnNewsCardClicked(article = article))
                                },
                                onRetry = {
                                    onEvent(NewsScreenEvent.OnCategoryChange(state.category))
                                }
                            )
                        }
                    }

                }
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
                onCardClick = { onCardClicked(article) }
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