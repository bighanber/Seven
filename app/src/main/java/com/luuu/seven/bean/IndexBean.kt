package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

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