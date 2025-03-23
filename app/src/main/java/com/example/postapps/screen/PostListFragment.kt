package com.example.postapps.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    onLoadMore: () -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            TopAppBar(
                title = { Text("Post List") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search by title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val message = (uiState as UiState.Error).message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Errore: $message")
                    }
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

                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        items(posts) { post ->
                            PostItem(post = post) {
                                onPostClick(post)
                            }
                        }
                    }
                }
            }
        }
    }
}
