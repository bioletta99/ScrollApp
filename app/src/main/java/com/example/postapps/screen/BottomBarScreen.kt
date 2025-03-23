package com.example.postapps.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : BottomBarScreen("posts", Icons.Filled.Home, "Home")
    data object Favorites : BottomBarScreen("favorites", Icons.Filled.Favorite, "Favorites")
}