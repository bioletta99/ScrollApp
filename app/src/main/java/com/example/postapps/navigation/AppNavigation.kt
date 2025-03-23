package com.example.postapps.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.postapps.screen.FavoriteFragment
import com.example.postapps.screen.PostDetailFragment
import com.example.postapps.screen.PostListFragment
import com.example.postapps.viewmodels.PostViewModel


fun NavGraphBuilder.appNavigation(
    navController: NavController,
    viewModel: PostViewModel
) {
    composable(Screen.PostList.route) {
        PostListFragment(
            onPostClick = { post -> navController.navigate(Screen.PostDetail.createRoute(post.id)) },
            onLoadMore = { viewModel.loadMorePosts() },
            viewModel = viewModel
        )
    }

    composable(Screen.PostDetail.route) { backStackEntry ->
        val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
        postId?.let {
            PostDetailFragment(
                postId = it,
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }

    composable(Screen.Favorites.route) {
        FavoriteFragment(
            onBack = { navController.popBackStack() },
            onPostClick = { post -> navController.navigate(Screen.PostDetail.createRoute(post.id)) },
            viewModel = viewModel
        )
    }
}
