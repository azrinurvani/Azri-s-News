package com.azrinurvani.azrisnews.domain.repository

import com.azrinurvani.azrisnews.domain.model.Article
import com.azrinurvani.azrisnews.util.Resource

interface NewsRepository {

    suspend fun getTopHeadlines(
    category : String
    ) : Resource<List<Article>> //using suspend and don't need return value using flow

    suspend fun searchForNews(
        query : String
    ) : Resource<List<Article>> //using suspend and don't need return value using flow
}