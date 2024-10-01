package com.azrinurvani.azrisnews.data.repository

import com.azrinurvani.azrisnews.data.remote.NewsApi
import com.azrinurvani.azrisnews.domain.model.Article
import com.azrinurvani.azrisnews.domain.repository.NewsRepository
import com.azrinurvani.azrisnews.util.Resource

class NewsRepositoryImpl ( //no need @Inject NewsApi class
// because have been inject in AppModule when provideNewsRepository
    private val newsApi: NewsApi
) : NewsRepository {
    override suspend fun getTopHeadlines(category: String): Resource<List<Article>> {
        return try{
            val response = newsApi.getBreakingNews(category = category)
            Resource.Success(response.articles)
        }catch (e: Exception){
            Resource.Error(message = "Failed to fetch news ${e.message}")
        }
    }

    override suspend fun searchForNews(query: String): Resource<List<Article>> {
        return try{
            val response = newsApi.searchForNews(query = query)
            Resource.Success(response.articles)
        }catch (e: Exception){
            Resource.Error(message = "Failed to fetch news ${e.message}")
        }
    }
}