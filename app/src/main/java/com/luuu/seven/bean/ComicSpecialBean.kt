package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicSpecialBean(
    var id: Int,
    var title: String,
    @SerializedName("short_title")
    var shortTitle: String,
    @SerializedName("create_time")
    var createTime: Int,
    @SerializedName("small_cover")
    var smallCover: String,
    @SerializedName("page_type")
    var pageType: Int,
    var sort: Int = 0,
    @SerializedName("page_url")
    var pageUrl: String
)