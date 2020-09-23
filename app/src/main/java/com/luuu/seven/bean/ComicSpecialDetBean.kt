package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicSpecialDetBean(
    @SerializedName("mobile_header_pic")
    var mobileHeaderPic: String,
    var title: String,
    var description: String,
    @SerializedName("comment_amount")
    var commentAmount: Int,
    var comics: List<ComicsSpecialDetInBean>
)

data class ComicsSpecialDetInBean (
    var cover: String,
    @SerializedName("recommend_brief")
    var recommendBrief: String,
    @SerializedName("recommend_reason")
    var recommendReason: String,
    var id: Int,
    var name: String,
    @SerializedName("alias_name")
    var aliasName: String
)
