package com.example.postapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.postapps.data.AppDatabase
import com.example.postapps.networks.RetrofitInstance
import com.example.postapps.screen.PostDetailFragment
import com.example.postapps.screen.PostListFragment
import com.example.postapps.ui.theme.PostAppsTheme
import com.example.postapps.viewmodels.PostViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostAppsTheme {
                val navController = rememberNavController()
                val database = AppDatabase.getDatabase(applicationContext)
                val postService = RetrofitInstance.api
                val viewModel: PostViewModel = viewModel(
                    factory = PostViewModelFactory(postService, database)
                )
                AppNavHost(navController, viewModel)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: PostViewModel) {
    NavHost(navController = navController, startDestination = "posts") {
        composable("posts") {
            PostListFragment(
                onPostClick = { post -> navController.navigate("details/${post.id}") },
                onLoadMore = { viewModel.loadMorePosts() },
                viewModel = viewModel
            )
        }
        composable("details/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
            postId?.let {
                PostDetailFragment(
                    postId = it,
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}