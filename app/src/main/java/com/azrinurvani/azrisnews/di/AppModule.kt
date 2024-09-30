package com.azrinurvani.azrisnews.di

import com.azrinurvani.azrisnews.data.remote.NewsApi
import com.azrinurvani.azrisnews.data.repository.NewsRepositoryImpl
import com.azrinurvani.azrisnews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNewsApi() : NewsApi {
        return Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    fun provideNewsRepository(
        newsApi: NewsApi
    ) : NewsRepository{
        return NewsRepositoryImpl(newsApi = newsApi)
    }
}