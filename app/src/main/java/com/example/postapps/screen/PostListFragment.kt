package com.example.postapps.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postapps.data.Post
import com.example.postapps.viewmodels.PostViewModel
import com.example.postapps.viewmodels.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListFragment(
    onPostClick: (Post) -> Unit,
    onFavoritesClick: () -> Unit,
    onLoadMore: () -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchQuery.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Post List") },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Default.Favorite, contentDescription = "Vai ai preferiti")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(8.dp)) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search by title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Errore: ${(uiState as UiState.Error).message}")
                }

                is UiState.Success -> {
                    val posts = (uiState as UiState.Success).posts

                    LaunchedEffect(listState) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .distinctUntilChanged()
                            .collectLatest { lastVisibleIndex ->
                                if (lastVisibleIndex == posts.lastIndex && posts.isNotEmpty()) {
                                    onLoadMore()
                                }
                            }
                    }

                    LazyColumn(state = listState) {
                        items(posts) { post ->
                            val isFav = favorites.any { it.id == post.id }
                            PostItem(
                                post = post,
                                isFavorite = isFav,
                                onClick = { onPostClick(post) },
                                onFavoriteClick = { viewModel.toggleFavorite(post) }
                            )
                        }
                    }
                }
            }
        }
    }
}