package com.example.postapps.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val favorites = viewModel.favorites.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(8.dp)) {
            items(favorites.value) { post ->
                PostItem(post = post, onClick = { onPostClick(post) })
            }
        }
    }
}
