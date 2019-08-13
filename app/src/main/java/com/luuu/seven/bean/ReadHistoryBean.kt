package com.luuu.seven.bean

data class ReadHistoryBean(
    var comicId: Int = 0,
    var chapterId: Int = 0,
    var chapterTitle: String = "",
    var browsePosition: Int = 0,
    var comicCover: String = "",
    var comicTitle: String = ""
)