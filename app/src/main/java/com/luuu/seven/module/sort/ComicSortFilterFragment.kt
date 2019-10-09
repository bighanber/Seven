package com.luuu.seven.module.sort

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.luuu.seven.R
import com.luuu.seven.adapter.SortFilterAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.FilterSection
import com.luuu.seven.bean.FilterViewBean
import com.luuu.seven.bean.SortFilterBean
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.activityViewModel
import com.luuu.seven.util.toast
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_EXPANDED
import com.luuu.seven.widgets.FilterView
import kotlinx.android.synthetic.main.fra_filter_layout.*

class ComicSortFilterFragment : BaseFragment() {

    private lateinit var viewModel: SortViewModel
    private var mAdapter: SortFilterAdapter? = null

    private val checkMap by lazy { HashMap<String, FilterViewBean>() }
    private val mAssemblyData = ArrayList<FilterSection>()
    private val mFlexLayoutManager: FlexboxLayoutManager by lazy {
        FlexboxLayoutManager(mContext).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.FLEX_START
        }
    }

    override fun initViews() {
        viewModel = activityViewModel<SortViewModel>().apply {
            sortFilterData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    assemblyData(it)
                    initAdapter()
                }
            })
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lp = filter_sheet.layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = BarUtils.getStatusBarHeight(context!!)
        filter_sheet.layoutParams = lp

        val behavior = BottomSheetBehavior.from(filter_sheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                super.onSlide(bottomSheet, slideOffset)
            }
        })

        filter_sheet.doOnLayout {
            val slideOffset = when(behavior.state) {
                STATE_EXPANDED -> 1f
                STATE_COLLAPSED -> 0f
                else /*BottomSheetBehavior.STATE_HIDDEN*/ -> -1f
            }
        }

        recycler_filter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                view_line.isActivated = recyclerView.canScrollVertically(-1)
            }
        })
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_filter_layout

    private fun assemblyData(data: List<SortFilterBean>) {
        mAssemblyData.clear()
        data.forEach { filter ->
            mAssemblyData.add(FilterSection(true, filter.title))
            filter.items.forEach { item ->
                mAssemblyData.add(FilterSection(item, filter.title))
            }
        }
    }

    private fun initAdapter() {
        mAdapter = SortFilterAdapter(mAssemblyData)
        recycler_filter.apply {
            layoutManager = mFlexLayoutManager
            adapter = mAdapter
        }

        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.filter_label) {
                val filterView = view as FilterView
                val check = !filterView.isChecked
                toast("$check")
                changeFilter(mAssemblyData[position].header)
                filterView.animateCheckedAndInvoke(check) {
                    mAssemblyData[position].t.check = true
                    checkMap[mAssemblyData[position].header] = FilterViewBean(filterView, position)
                }
            }
        }

    }


    private fun changeFilter(key: String) {

        checkMap[key]?.let { bean ->
            bean.view.animateCheckedAndInvoke(false) {
                mAssemblyData[bean.position].t.check = false
            }
        }

//        mAssemblyData.filterNot {
//            it.isHeader
//        }.groupBy {
//            it.header
//        }.filter {
//            it.value.find { section ->
//                section.t.tagId == mAssemblyData[position].t.tagId
//            } != null
//        }.flatMap {
//            it.value
//        }.forEachIndexed { index, filterSection ->
//            if (filterSection.t.check) {
//                (mAdapter?.getViewByPosition(recycler_filter, index, R.id.filter_label) as? FilterView)?.animateCheckedAndInvoke(false) {
//                    filterSection.t.check = false
//                }
//            }
//        }

    }


    override fun onDetach() {
        super.onDetach()
        toast("close")
    }
}