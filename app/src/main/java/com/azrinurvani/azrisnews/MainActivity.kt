package com.azrinurvani.azrisnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.azrinurvani.azrisnews.presentation.news_screen.NewsScreen
import com.azrinurvani.azrisnews.presentation.theme.AzrisNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            AzrisNewsTheme {
                NewsScreen()
            }
        }
    }
}

