package com.example.postapps.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_posts")
data class FavoritePost(
    @PrimaryKey val id: Int,
    val title: String,
    val body: String
)
