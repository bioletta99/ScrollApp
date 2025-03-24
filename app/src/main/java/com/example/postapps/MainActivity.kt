package com.example.postapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.postapps.data.AppDatabase
import com.example.postapps.networks.RetrofitInstance
import com.example.postapps.screen.BottomBarScreen
import com.example.postapps.screen.FavoriteFragment
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

                val screens = listOf(
                    BottomBarScreen.Home,
                    BottomBarScreen.Favorites
                )
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStack?.destination?.route

                Scaffold(
                    bottomBar = {
                        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
                            screens.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                                    label = { Text(screen.label) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                    ),
                                    selected = currentDestination == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomBarScreen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomBarScreen.Home.route) {
                            PostListFragment(
                                onPostClick = { post ->
                                    navController.navigate("details/${post.id}")
                                },
                                onLoadMore = { viewModel.loadMorePosts() },
                                onFavoritesClick = {viewModel.favorites},
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
                        composable(BottomBarScreen.Favorites.route) {
                            FavoriteFragment(
                                onBack = { /* disabilitato il back per i preferiti */ },
                                onPostClick = { post ->
                                    navController.navigate("details/${post.id}")
                                },
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
