package com.example.postapps.screen

import com.example.postapps.data.FavoritePost
import com.example.postapps.data.Post


fun Post.toFavoritePost(): FavoritePost {
    return FavoritePost(
        id = this.id,
        title = this.title,
        body = this.body
    )
}
