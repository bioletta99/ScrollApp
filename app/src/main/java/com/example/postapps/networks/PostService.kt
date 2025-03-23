package com.example.postapps.networks

import com.example.postapps.data.Post
import retrofit2.http.GET



interface PostService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}

