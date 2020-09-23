package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class HotComicBean(
    @SerializedName("comic_id")
    var comicId: Int,
    var title: String,
    var authors: String,
    var status: String,
    var cover: String,
    var types: String,
    @SerializedName("last_updatetime")
    var lastUpdatetime: Long,
    var num: Int = 0
)