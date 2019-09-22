package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

data class CimicRelatedBean(
    @SerializedName("author_comics")
    val authorComics: List<AuthorComic>,
    val novels: List<ComicRelatedInfoBean>,
    @SerializedName("theme_comics")
    val themeComics: List<ComicRelatedInfoBean>
)

data class AuthorComic(
    @SerializedName("author_id")
    val authorId: Int,
    @SerializedName("author_name")
    val authorName: String,
    val data: List<ComicRelatedInfoBean>
)

data class ComicRelatedInfoBean(
    val cover: String,
    val id: Int,
    val name: String,
    val status: String
)
