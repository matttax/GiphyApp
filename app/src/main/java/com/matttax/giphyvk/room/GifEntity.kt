package com.matttax.giphyvk.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("images")
data class GifEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo("title")
    var title: String,

    @ColumnInfo("url")
    var url: String
)