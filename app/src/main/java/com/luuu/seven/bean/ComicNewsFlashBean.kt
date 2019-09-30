package com.luuu.seven.bean

import com.google.gson.annotations.SerializedName

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