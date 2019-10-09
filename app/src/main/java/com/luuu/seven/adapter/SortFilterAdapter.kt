package com.luuu.seven.adapter

import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.luuu.seven.R
import com.luuu.seven.bean.FilterSection
import com.luuu.seven.widgets.FilterView

class SortFilterAdapter(data: List<FilterSection>) : BaseSectionQuickAdapter<FilterSection, BaseViewHolder>(R.layout.item_sort_filter, R.layout.item_sort_filter_header, data) {

    override fun convertHead(helper: BaseViewHolder?, item: FilterSection?) {
        helper?.setText(R.id.filter_title, item?.header)
    }

    override fun convert(helper: BaseViewHolder, item: FilterSection?) {
//        val data = item?.t
        helper.getView<FilterView>(R.id.filter_label).text = item?.t?.tagName ?: ""
        val filterView = helper.getView<FilterView>(R.id.filter_label)
        val params = filterView.layoutParams
        if(params is FlexboxLayoutManager.LayoutParams) params.flexGrow = 1.0f
        helper.addOnClickListener(R.id.filter_label)

        if (item?.t?.check == false) {
            filterView.isChecked = false
        }
    }


}