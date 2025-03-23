package com.example.postapps.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postapps.data.AppDatabase
import com.example.postapps.data.Post
import com.example.postapps.networks.PostService
import com.example.postapps.screen.toFavoritePost
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class UiState {
    data object Loading : UiState()
    data class Success(val posts: List<Post>) : UiState()
    data class Error(val message: String) : UiState()
}

class PostViewModel(
    private val postService: PostService,
    private val database: AppDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _allPosts = MutableStateFlow<List<Post>>(emptyList())
    private val _favorites = MutableStateFlow<List<Post>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val favorites: StateFlow<List<Post>> = _favorites
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredPosts: StateFlow<List<Post>> = combine(_allPosts, _searchQuery) { posts, query ->
        if (query.isBlank()) posts
        else posts.filter { it.title.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var currentPage = 1
    private val pageSize = 20

    init {
        fetchPosts()
        loadFavorites()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val posts = postService.getPosts()  // <--- senza parametri
                _allPosts.value = posts
                _uiState.value = UiState.Success(posts)
            } catch (e: IOException) {
                _uiState.value = UiState.Error("Errore di rete.")
            } catch (e: HttpException) {
                _uiState.value = UiState.Error("Errore del server.")
            }
        }
    }


    fun loadMorePosts() {
        currentPage++
        fetchPosts()
    }

    fun toggleFavorite(post: Post) {
        viewModelScope.launch {
            val dao = database.favoritePostDao()
            val isFavorite = _favorites.value.any { it.id == post.id }
            if (isFavorite) {
                dao.delete(post.toFavoritePost())
            } else {
                dao.insert(post.toFavoritePost())
            }
            loadFavorites()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addToFavorites(post: Post) {
        viewModelScope.launch {
            database.favoritePostDao().insert(post.toFavoritePost())
            loadFavorites()
        }
    }

    fun removeFromFavorites(post: Post) {
        viewModelScope.launch {
            database.favoritePostDao().delete(post.toFavoritePost())
            loadFavorites()
        }
    }

    fun isFavorite(postId: Int): StateFlow<Boolean> {
        val isFavoriteFlow = MutableStateFlow(false)
        viewModelScope.launch {
            isFavoriteFlow.value = _favorites.value.any { it.id == postId }
        }
        return isFavoriteFlow
    }

    fun getPostById(postId: Int): StateFlow<Post?> {
        val postFlow = MutableStateFlow<Post?>(null)
        viewModelScope.launch {
            val post = _allPosts.value.find { it.id == postId }
            postFlow.value = post
        }
        return postFlow
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            database.favoritePostDao().getAllFavorites().collect { favoritePosts ->
                _favorites.value = favoritePosts.map {
                    Post(id = it.id, title = it.title, body = it.body)
                }
            }
        }
    }
}
