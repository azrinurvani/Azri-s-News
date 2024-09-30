package com.azrinurvani.azrisnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import com.azrinurvani.azrisnews.presentation.news_screen.NewsScreen
import com.azrinurvani.azrisnews.presentation.theme.AzrisNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            AzrisNewsTheme {
                NewsScreen()
            }
        }
    }
}

