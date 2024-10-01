package com.azrinurvani.azrisnews.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchIconClicked : () -> Unit
){
    TopAppBar(
        title = {
            Text(
                text = "Azri's News",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun NewsScreenTopBarPrev(){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    NewsScreenTopBar(
        scrollBehavior = scrollBehavior,
        onSearchIconClicked = {}
    )
}