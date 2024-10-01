package com.azrinurvani.azrisnews.presentation.news_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azrinurvani.azrisnews.domain.model.Article
import com.azrinurvani.azrisnews.domain.repository.NewsRepository
import com.azrinurvani.azrisnews.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

//    var articles by mutableStateOf<List<Article>>(emptyList())

    var state by mutableStateOf(NewsScreenState())

    private var searchJob : Job? = null

    init {
        getNewsArticles(category = "general")
    }

    fun onEvent(event : NewsScreenEvent){
        when(event){
            is NewsScreenEvent.OnCategoryChange -> {
                state = state.copy(category = event.category)
                getNewsArticles(category = state.category)
            }
            is NewsScreenEvent.OnNewsCardClicked -> {
                state = state.copy(selectedArticle = event.article)
            }
            NewsScreenEvent.OnSearchIconClicked -> {
                state = state.copy(
                    isSearchBarVisible = true,
                    articles = emptyList()
                )
            }
            NewsScreenEvent.OnCloseIconClicked -> {
                state = state.copy(isSearchBarVisible = false)
                getNewsArticles(category = state.category)
            }
            is NewsScreenEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.searchQuery)
                searchJob?.cancel() //search job handle state UI and no execute directly after query changes
                searchJob = viewModelScope.launch { //need launch scope for process query before call search api with delay 1000 ms
                    delay(1000)
                    searchForNews(query = state.searchQuery)
                }

            }
        }
    }

    private fun getNewsArticles(category:String){
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = newsRepository.getTopHeadlines(category = category)
            when(result){
                is Resource.Success ->{
                    state = state.copy( //copy result data to state of category
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }
            }
        }
    }

    private fun searchForNews(query:String){
        if (query.isEmpty()){
            return
        }
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = newsRepository.searchForNews(query = query)
            when(result){
                is Resource.Success ->{
                    state = state.copy( //copy result data to state of category
                        articles = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error ->{
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        articles = emptyList()
                    )
                }
            }
        }
    }
}