package com.matttax.giphyvk.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GifDao {
    @Insert
    fun insertGif(gif: GifEntity)

    @Query("SELECT * FROM images")
    fun getAll(): Flow<List<GifEntity>>
}