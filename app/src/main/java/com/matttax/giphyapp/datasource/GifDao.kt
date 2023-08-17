package com.matttax.giphyapp.datasource

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface GifDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGif(gif: GifEntity)

    @Query("SELECT * FROM images")
    fun getAll(): Single<List<GifEntity>>

    @Update(entity = GifEntity::class)
    fun rename(titleTuple: TitleTuple)

    @Query("DELETE FROM images WHERE url=:url")
    fun deleteByUrl(url: String)
}
