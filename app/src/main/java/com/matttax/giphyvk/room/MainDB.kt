package com.matttax.giphyvk.room

import android.content.Context
import androidx.room.*

@Database(entities = [GifEntity::class], version = 1)
abstract class MainDB: RoomDatabase() {

    abstract fun getDao(): GifDao

    companion object {
        fun getDB(context: Context): MainDB {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDB::class.java,
                "giphy.db"
            ).build()
        }
    }
}