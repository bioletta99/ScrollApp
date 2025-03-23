package com.example.postapps.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.postapps.viewmodels.PostViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PostList.route,
        modifier = modifier
    ) {
        appNavigation(navController, viewModel)
    }
}

