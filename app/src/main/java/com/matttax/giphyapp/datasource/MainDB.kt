package com.matttax.giphyapp.datasource

import android.content.Context
import androidx.room.*

@Database(entities = [GifEntity::class], version = 1)
abstract class MainDB: RoomDatabase() {

    abstract fun getGifDao(): GifDao

    companion object {
        private const val DATABASE_NAME = "giphy.db"

        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}