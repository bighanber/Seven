package com.luuu.seven.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comic_collect")
data class CollectBean(
    @PrimaryKey @ColumnInfo(name = "comic_id")
    var comicId: Int = 0,
    @ColumnInfo(name = "comic_title")
    var comicTitle: String = "",
    @ColumnInfo(name = "comic_authors")
    var comicAuthors: String = "",
    @ColumnInfo(name = "cover")
    var comicCover: String = "",
    @ColumnInfo(name = "collect_time")
    var collectTime: Long = 0
)