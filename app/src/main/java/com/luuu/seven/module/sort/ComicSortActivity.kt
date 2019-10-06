package com.luuu.seven.module.sort

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicSortAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.bean.FilterSection
import com.luuu.seven.bean.SortFilterBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.click
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.util.startActivity
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_sort_layout.*


class ComicSortActivity : BaseActivity() {

    private lateinit var mViewModel: SortViewModel
    private var mSortBeanList: ArrayList<ComicSortListBean> = ArrayList()


    private var mAdapter: ComicSortAdapter? = null
    private var mPageNum = 0
    private var mFilter = "0"
    private val mLayoutManager by lazy { GridLayoutManager(mContext, 3) }

    override fun initViews() {

        swipe_refresh.setOnRefreshListener {

        }

        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.filter_sheet))
        filter_fab.click {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val a11yState = if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
                } else {
                    View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
                }
//                comic_recyclerview.importantForAccessibility = a11yState
            }
        })
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.skipCollapsed = true
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        mViewModel = obtainViewModel<SortViewModel>().apply {
            getSortComicFilter()
            getSortComicList("0", 0)

            sortListData.observe(this@ComicSortActivity, Observer {
                mSortBeanList.addAll(it)
                mAdapter?.let { adapter ->
                    adapter.loadMoreComplete()
                    if (swipe_refresh.isRefreshing) {
                        swipe_refresh.isRefreshing = false
                    } else {
                        if (it.isEmpty()) {
                            adapter.loadMoreEnd()
                        } else {
                            adapter.notifyDataSetChanged()
                        }
                    }
                } ?: initAdapter()
            })
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_sort_layout

    private fun initAdapter() {
        mAdapter = ComicSortAdapter(mSortBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mViewModel.getSortComicList(mFilter, mPageNum)
            }, comic_list)

            setOnItemClickListener { _, _, position ->
                startActivity<ComicIntroActivity>(ComicIntroActivity.COMIC_ID to mSortBeanList[position].id)
            }
        }

        comic_list.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
            addItemDecoration(SpaceItemDecoration(mContext).setSpace(10))
        }
    }


//    private fun showSortDialog() {
//        if (mBottomSheetDialog == null) {
//            mBottomSheetDialog = BottomSheetDialog(this)
//            val view = layoutInflater.inflate(R.layout.sort_dialog_layout, null)
//            val recyclerView = view.findViewById<View>(R.id.recycler_sort_dialog) as RecyclerView
//            recyclerView.setHasFixedSize(true)
//            recyclerView.layoutManager = GridLayoutManager(this, 3)
//            val adapter = ComicSortDialogAdapter(R.layout.item_chapter_layout, mSortBeanList!!)
//            recyclerView.adapter = adapter
//            adapter.setOnItemClickListener { _, _, position ->
////                mBottomSheetDialog!!.dismiss()
////                val tab = tabs.getTabAt(position)
////                tab?.select()
//            }
//            mBottomSheetDialog!!.setContentView(view)
//
//            // 解决下滑隐藏dialog 后，再次调用show 方法显示时，不能弹出Dialog
//            val view1 = mBottomSheetDialog!!.delegate.findViewById<View>(R.id.design_bottom_sheet)
//            val bottomSheetBehavior = BottomSheetBehavior.from(view1)
//            bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//                }
//
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                        mBottomSheetDialog!!.dismiss()
//                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                    }
//                }
//
//            })
//            mBottomSheetDialog!!.show()
//        } else {
//            mBottomSheetDialog!!.show()
//        }
//    }

}
