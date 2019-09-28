package com.luuu.seven.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_read")
data class ReadHistoryBean(
    @PrimaryKey @ColumnInfo(name = "comic_id")
    var comicId: Int = 0,
    @ColumnInfo(name = "chapter_id")
    var chapterId: Int = 0,
    @ColumnInfo(name = "chapter_title")
    var chapterTitle: String = "",
    @ColumnInfo(name = "browse_position")
    var browsePosition: Int = 0,
    @ColumnInfo(name = "cover")
    var comicCover: String = "",
    @ColumnInfo(name = "comic_title")
    var comicTitle: String = ""
)