package com.example.postapps.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postapps.data.Post
import com.example.postapps.viewmodels.PostViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteFragment(
    onBack: () -> Unit,
    onPostClick: (Post) -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Preferiti") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(modifier = Modifier
                .padding(padding)
                .fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessun preferito trovato")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(favorites) { post ->
                    PostItem(
                        post = post,
                        isFavorite = true,
                        onClick = { onPostClick(post) },
                        onFavoriteClick = { viewModel.removeFromFavorites(post) }
                    )
                }
            }
        }
    }
}
