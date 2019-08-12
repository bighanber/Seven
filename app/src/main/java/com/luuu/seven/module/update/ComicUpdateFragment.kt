package com.luuu.seven.module.update

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicUpdateAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.DataLoadType
import kotlinx.android.synthetic.main.fra_tab_layout.*


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ComicUpdateFragment : BaseFragment(), ComicUpdateContract.View {

    companion object {
        private val Comic_TYPE = "type"

        fun newInstance(type: String): ComicUpdateFragment {
            val fragment = ComicUpdateFragment()
            val bundle = Bundle()
            bundle.putString(Comic_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mUpdateBeanList: MutableList<ComicUpdateBean>? = null
    private var mPageNum = 0
    private var num = 100
    private var type = " "
    private val mPresent by lazy { ComicUpdatePresenter(this) }
    private var mAdapter: ComicUpdateAdapter? = null
    private val mLayoutManager by lazy { GridLayoutManager(mContext, 3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(Comic_TYPE)
            num = when(type) {
                "全部" -> 100
                "原创" -> 1
                else -> 0
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresent.unsubscribe()
        mUpdateBeanList = null
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) {
            mUpdateBeanList = ArrayList()
            mPageNum = 0
            mPresent.getComicUpdate(num, 0)
        }
    }

    override fun initViews() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mPresent.refreshData(num)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onFirstUserInvisible() {
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateComicList(data: List<ComicUpdateBean>, type: DataLoadType) {
        when(type) {
            DataLoadType.TYPE_REFRESH_SUCCESS -> {
                mUpdateBeanList = data.toMutableList()
                if (mAdapter == null) {
                    initAdapter(data)
                } else {
                    mAdapter!!.setNewData(data)
                }
            }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS -> {
                if (data.isEmpty()) {
                    showToast("没有数据咯")
                    mAdapter!!.loadMoreEnd()
                } else {
                    mAdapter!!.addData(data)
                    mAdapter!!.loadMoreComplete()
                    mUpdateBeanList!!.addAll(data)
                }
            }
            else -> {
            }
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        refresh.isEnabled = isRefresh
    }

    private fun initAdapter(updateBeanList: List<ComicUpdateBean>) {
        mAdapter = ComicUpdateAdapter(R.layout.item_comic_layout, updateBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mPresent.loadMoreData(num, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mUpdateBeanList!![position].id)
                startNewActivity(ComicIntroActivity::class.java, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


    }
}