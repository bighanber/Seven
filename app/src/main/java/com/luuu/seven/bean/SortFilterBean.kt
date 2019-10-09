package com.luuu.seven.bean

import com.chad.library.adapter.base.entity.SectionEntity
import com.google.gson.annotations.SerializedName

data class SortFilterBean(
    val items: List<SortFilterItemBean>,
    val title: String
)

data class SortFilterItemBean(
    @SerializedName("tag_id")
    val tagId: Int,
    @SerializedName("tag_name")
    val tagName: String,
    var check: Boolean = false
)

class FilterSection : SectionEntity<SortFilterItemBean> {

    constructor(isHeader: Boolean, header: String) : super(isHeader, header)

    constructor(sortFilterItemBean: SortFilterItemBean, header: String) : super(sortFilterItemBean) {
        this.header = header
    }
}
