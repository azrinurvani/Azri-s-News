package com.azrinurvani.azrisnews.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.azrinurvani.azrisnews.presentation.article_screen.ArticleScreen
import com.azrinurvani.azrisnews.presentation.news_screen.NewsScreen
import com.azrinurvani.azrisnews.presentation.news_screen.NewsScreenViewModel

@Composable
fun NavGraphSetup(
    navHostController: NavHostController
){

    val argKey = "web_url"

    NavHost(
        navController = navHostController,
        startDestination = "news_screen"
    ){
        composable(route = "news_screen"){
            val viewModel : NewsScreenViewModel = hiltViewModel()
            NewsScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                onReadFullStoryReadButtonClicked = { url ->
                    navHostController.navigate("article_screen?$argKey=$url")
                }
            )
        }

        composable(
            route = "article_screen?$argKey={web_url}",
            arguments = listOf(navArgument(name = argKey){
                type = NavType.StringType
            })
        ){ backStageEntry ->
            ArticleScreen(
                url = backStageEntry.arguments?.getString(argKey),
                onBackPressed = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}