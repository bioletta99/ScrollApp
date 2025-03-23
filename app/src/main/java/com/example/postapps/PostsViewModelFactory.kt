package com.example.postapps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postapps.data.AppDatabase
import com.example.postapps.networks.PostService
import com.example.postapps.viewmodels.PostViewModel


class PostViewModelFactory(
    private val postService: PostService,
    private val database: AppDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostViewModel(postService, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}





