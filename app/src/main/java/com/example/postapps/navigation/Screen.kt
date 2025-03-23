package com.example.postapps.navigation


sealed class Screen(val route: String) {
    data object PostList : Screen("post_list")
    data object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: Int) = "post_detail/$postId"
    }
    data object Favorites : Screen("favorites")
}
