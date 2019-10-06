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
    val tagName: String
)

class FilterSection : SectionEntity<SortFilterItemBean> {

    constructor(isHeader: Boolean, header: String) : super(isHeader, header)

    constructor(sortFilterItemBean: SortFilterItemBean) : super(sortFilterItemBean)
}
