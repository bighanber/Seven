package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

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