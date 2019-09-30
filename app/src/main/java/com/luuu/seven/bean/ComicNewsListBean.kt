package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicNewsListBean (
    var title: String,
    @SerializedName("from_name")
    var fromName: String,
    @SerializedName("from_url")
    var fromUrl: String,
    @SerializedName("create_time")
    var createTime: Long,
    @SerializedName("is_foreign")
    var isForeign: Int,
    @SerializedName("foreign_url")
    var foreignUrl: String,
    var intro: String,
    @SerializedName("author_id")
    var authorId: Int,
    var status: Int,
    @SerializedName("row_pic_url")
    var rowPicUrl: String,
    @SerializedName("col_pic_url")
    var colPicUrl: String,
    @SerializedName("article_id")
    var articleId: Int,
    @SerializedName("page_url")
    var pageUrl: String,
    @SerializedName("comment_amount")
    var commentAmount: String,
    @SerializedName("author_uid")
    var authorUid: Int,
    var cover: String,
    var nickname: String,
    @SerializedName("mood_amount")
    var moodAmount: Int
)