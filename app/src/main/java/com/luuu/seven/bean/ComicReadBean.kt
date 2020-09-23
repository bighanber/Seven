package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicReadBean (
    @SerializedName("chapter_id")
    var chapterId: Int,
    @SerializedName("comic_id")
    var comicId: Int,
    var title: String,
    @SerializedName("chapter_order")
    var chapterOrder: Int,
    var direction: Int,
    var picnum: Int,
    @SerializedName("comment_count")
    var commentCount: Int,
    @SerializedName("page_url")
    var pageUrl: MutableList<String>
)