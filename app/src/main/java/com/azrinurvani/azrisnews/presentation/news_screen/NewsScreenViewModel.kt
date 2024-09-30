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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsScreenViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var articles by mutableStateOf<List<Article>>(emptyList())

    var state by mutableStateOf(NewsScreenState())

    init {
        getNewsArticles(category = "general")
    }

    fun onEvent(event : NewsScreenEvent){
        when(event){
            is NewsScreenEvent.OnCategoryChange -> {
                state = state.copy(category = event.category)
                getNewsArticles(category = state.category)
            }
            NewsScreenEvent.OnCloseIconClicked -> TODO()
            is NewsScreenEvent.OnNewsCardClicked -> TODO()
            NewsScreenEvent.OnSearchIconClicked -> TODO()
            is NewsScreenEvent.OnSearchQueryChange -> TODO()
        }
    }

    private fun getNewsArticles(category:String){
        viewModelScope.launch {
            val result = newsRepository.getTopHeadlines(category = category)
            when(result){
                is Resource.Success ->{
                    state = state.copy( //copy result data to state of category
                        articles = result.data ?: emptyList()
                    )
                }
                is Resource.Error ->{

                }
            }
        }
    }
}