package com.azrinurvani.azrisnews.presentation.news_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.azrinurvani.azrisnews.presentation.component.NewsArticleCard

@Composable
fun NewsScreen(
    viewModel: NewsScreenViewModel = hiltViewModel<NewsScreenViewModel>()
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(viewModel.articles){ article->
            NewsArticleCard(
                article = article,
                onCardClick = {  }
            )
        }
    }
}