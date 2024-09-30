package com.azrinurvani.azrisnews.presentation.news_screen

import com.azrinurvani.azrisnews.domain.model.Article

sealed class NewsScreenEvent {

    data class OnNewsCardClicked(val article: Article) : NewsScreenEvent()
    data class OnCategoryChange(val category: String) : NewsScreenEvent()
    data class OnSearchQueryChange(val searchQuery: String) : NewsScreenEvent()
    object OnSearchIconClicked : NewsScreenEvent()
    object OnCloseIconClicked : NewsScreenEvent()
}