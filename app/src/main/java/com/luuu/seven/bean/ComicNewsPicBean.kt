package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

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