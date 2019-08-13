package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
data class IndexBean(
//        @SerializedName("category_id")
        val categoryId: Int,
        val title: String,
        val sort: Int,
        val data: List<IndexDataBean>)

data class IndexDataBean(
        val cover: String,
        val title: String,
        @SerializedName("sub_title")
        val subTitle: String,
        val type: Int,
        val url: String,
        @SerializedName("obj_id")
        val objId: Int,
        val status: String)

data class CollectBean(
        var comicId: Int = 0,
        var comicTitle: String = "",
        var comicAuthors: String = "",
        var comicCover: String = "",
        var collectTime: Long = 0
)

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

data class ComicNewsPicBean (
        var code: Int,
        var msg: String,
        var data: List<ComicNewsPicDataBean>
)

data class ComicNewsPicDataBean (
        var id: Int,
        var title: String,
        @SerializedName("pic_url")
        var picUrl: String,
        @SerializedName("object_id")
        var objectId: Int,
        @SerializedName("object_url")
        var objectUrl: String
)

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

data class ComicNewsFlashBean (
        var id: Int,
        var uid: Int,
        var nickname: String,
        var content: String,
        var updatetime: Int,
        var img: String,
        var cover: String,
        @SerializedName("vote_amount")
        var voteAmount: Int
)

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
        var chapters: List<ChaptersBean>,
        var mReadHistoryBean: ReadHistoryBean? = null
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

data class SearchDataBean (
        var id: Int,
        var status: String,
        var title: String,
        @SerializedName("last_name")
        var lastName: String,
        var cover: String,
        var authors: String,
        var types: String,
        @SerializedName("hot_hits")
        var hotHits: Long
)

data class HotSearchBean (
        var id: Int,
        var name: String
)

