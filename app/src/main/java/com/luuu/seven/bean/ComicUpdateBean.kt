package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicUpdateBean(
    var id: Int,
    var title: String,
    var islong: Int,
    var authors: String,
    var types: String,
    var cover: String,
    var status: String,
    @SerializedName("last_update_chapter_name")
    var lastUpdateChapterName: String,
    @SerializedName("last_update_chapter_id")
    var lastUpdateChapterId: Int,
    @SerializedName("last_updatetime")
    var lastUpdatetime: Int
)