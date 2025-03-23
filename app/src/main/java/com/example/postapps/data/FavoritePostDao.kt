package com.example.postapps.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritePostDao {
    @Query("SELECT * FROM favorite_posts")
    fun getAllFavorites(): Flow<List<FavoritePost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: FavoritePost)

    @Delete
    suspend fun delete(post: FavoritePost)
}
