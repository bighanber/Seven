package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class ComicIntroBean (
    var id: Int,
    var islong: Int,
    var direction: Int,
    var title: String,
    @SerializedName("is_dmzj")
    var isDmzj: Int,
    var cover: String,
    var description: String,
    @SerializedName("last_updatetime")
    var lastUpdatetime: Long,
    var copyright: Int,
    @SerializedName("first_letter")
    var firstLetter: String,
    @SerializedName("hot_num")
    var hotNum: Long,
    @SerializedName("hit_num")
    var hitNum: Long,
    var uid: String,
    @SerializedName("subscribe_num")
    var subscribeNum: Int,
    var comment: CommentBean,
    var types: List<TypesBean>,
    var authors: List<AuthorsBean>,
    var status: List<StatusBean>,
    var chapters: List<ChaptersBean>
)

data class CommentBean (
    @SerializedName("comment_count")
    var commentCount: Int,
    @SerializedName("latest_comment")
    var latestComment: List<LatestCommentBean>
)

data class LatestCommentBean (
    @SerializedName("comment_id")
    var commentId: Int,
    var uid: Long,
    var content: String,
    var createtime: Long,
    var nickname: String,
    var avatar: String
)

data class TypesBean (
    @SerializedName("tag_id")
    var tagId: Int,
    @SerializedName("tag_name")
    var tagName: String
)

data class AuthorsBean (
    @SerializedName("tag_id")
    var tagId: Int,
    @SerializedName("tag_name")
    var tagName: String
)

data class StatusBean (
    @SerializedName("tag_id")
    var tagId: Int,
    @SerializedName("tag_name")
    var tagName: String
)

data class ChaptersBean (
    var title: String,
    var data: ArrayList<ChapterDataBean>
)