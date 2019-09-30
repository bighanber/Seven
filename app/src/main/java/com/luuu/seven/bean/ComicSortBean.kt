package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicSortBean (
    @SerializedName("tag_id")
    var tagId: Int,
    var title: String,
    var cover: String
)

data class ComicSortListBean (
    var id: Int,
    var title: String,
    var authors: String,
    var status: String,
    var cover: String,
    var types: String,
    @SerializedName("last_updatetime")
    var lastUpdatetime: Long,
    var num: Int
)