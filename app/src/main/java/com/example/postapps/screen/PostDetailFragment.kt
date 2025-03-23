package com.example.postapps.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postapps.viewmodels.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailFragment(
    postId: Int,
    onBack: () -> Unit,
    viewModel: PostViewModel = viewModel()
) {
    val post by viewModel.getPostById(postId).collectAsState(initial = null)
    val isFavorite by viewModel.isFavorite(postId).collectAsState(initial = false)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dettaglio Post") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        post?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(text = it.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.body, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isFavorite) viewModel.removeFromFavorites(it)
                        else viewModel.addToFavorites(it)
                    }
                ) {
                    Text(
                        text = if (isFavorite) "Rimuovi dai preferiti"
                        else "Aggiungi ai preferiti"
                    )
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Post non trovato")
        }
    }
}
