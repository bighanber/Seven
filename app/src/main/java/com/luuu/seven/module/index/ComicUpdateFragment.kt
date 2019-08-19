package com.luuu.seven.module.index

import android.os.Bundle
import android.util.TypedValue
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicUpdateAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.dp2px
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.widgets.SpaceItemDecoration
import kotlinx.android.synthetic.main.fra_tab_layout.*


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ComicUpdateFragment : BaseFragment() {

    private lateinit var mViewModel: HomeViewModel
    private var mUpdateBeanList: ArrayList<ComicUpdateBean> = ArrayList()
    private var mPageNum = 0
    private var num = 100
    private var mAdapter: ComicUpdateAdapter? = null
    private val mLayoutManager by lazy { GridLayoutManager(mContext, 3) }

    companion object {
        fun newInstance(): ComicUpdateFragment {
            return ComicUpdateFragment()
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onResume() {
        super.onResume()
        mUpdateBeanList = ArrayList()
        mPageNum = 0
        mViewModel.getComicUpdate(num, 0)
    }

    override fun initViews() {

        val typedValue = TypedValue()
        val mActonBarHeight = if (context!!.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        else 0
        val mTopPadding = BarUtils.getStatusBarHeight(activity!!) + mActonBarHeight
        recycler.setPadding(mContext!!.dp2px(15), mTopPadding, mContext!!.dp2px(15), 0)

        mViewModel = obtainViewModel(HomeViewModel::class.java).apply {
            updateData.observe(viewLifecycleOwner, Observer { data ->
                mUpdateBeanList.addAll(data)

                mAdapter?.let { adapter ->

                    adapter.loadMoreComplete()
                    if (refresh.isRefreshing) {
                        refresh.isRefreshing = false
                    } else {
                        if (data.isEmpty()) {
                            adapter.loadMoreEnd()
                        } else {
                            adapter.notifyDataSetChanged()
                        }
                    }
                } ?: initAdapter()
            })
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mViewModel.getComicUpdate(num, mPageNum)
        }
    }

    private fun initAdapter() {
        mAdapter = ComicUpdateAdapter(R.layout.item_comic_layout, mUpdateBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mViewModel.getComicUpdate(num, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mUpdateBeanList[position].id)
                startNewActivity(ComicIntroActivity::class.java, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
        recycler.addItemDecoration(SpaceItemDecoration(mContext!!).setSpace(10))


    }

}