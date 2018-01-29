package com.luuu.seven.module.rank

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicRankAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.DataLoadType
import com.luuu.seven.util.loadImg
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicRankFragment : BaseFragment(), ComicRankContract.View {

    companion object {
        private val Comic_TYPE = "type"

        fun newInstance(type: String): ComicRankFragment {
            val fragment = ComicRankFragment()
            val bundle = Bundle()
            bundle.putString(Comic_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }

    }

    private var mHotComicBeanList: MutableList<HotComicBean>? = null
    private var mHotComicTopList: MutableList<HotComicBean>? = null
    private var mPageNum = 0
    private var pos = 0
    private var num = 0
    private var type = " "
    private val mPresent by lazy { ComicRankPresenter(this) }
    private lateinit var headerView: View
    private var mAdapter: ComicRankAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    override fun onFirstUserVisible() {
        mHotComicBeanList = ArrayList()
        mHotComicTopList = ArrayList()
        mPageNum = 0
        mPresent.getComicData(num, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(Comic_TYPE)
            num = when(type) {
                "人气" -> 0
                "吐槽" -> 1
                else -> 2
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresent.unsubscribe()
        mHotComicTopList = null
        mHotComicBeanList = null
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun initViews() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
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

    override fun updateComicList(data: List<HotComicBean>, type: DataLoadType) {
        when(type) {
            DataLoadType.TYPE_REFRESH_SUCCESS -> {
                headerView = getHeaderView(data, View.OnClickListener { view ->
                    pos = when(view.id) {
                        R.id.iv_rank_top -> 0
                        R.id.iv_rank_sec -> 1
                        R.id.iv_rank_thr -> 2
                        else -> 0
                    }
                    val mBundle = Bundle()
                    mBundle.putInt("comicId", mHotComicTopList!![pos].comicId)
                    startNewActivity(ComicIntroActivity::class.java, mBundle)
                })
                mHotComicTopList!!.clear()
                for (i in 0..2) {
                    mHotComicTopList!!.add(data[0])
                }
                val mData = data.drop(3)
                mHotComicBeanList = mData.toMutableList()
                if (mAdapter == null) {
                    initAdapter(mData)
                } else {
                    mAdapter!!.setNewData(mData)
                }
            }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS -> {
                if (data.isEmpty()) {
                    showToast("没有数据咯")
                    mAdapter!!.loadMoreEnd()
                } else {
                    mAdapter!!.addData(data)
                    mAdapter!!.loadMoreComplete()
                    mHotComicBeanList!!.addAll(data)
                }
            }
            else -> {
            }
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        refresh.isEnabled = isRefresh
    }

    private fun initAdapter(hotComicBeanList: List<HotComicBean>) {
        mAdapter = with(ComicRankAdapter(R.layout.item_rank_layout, hotComicBeanList)) {
            addHeaderView(headerView)
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mPresent.loadMoreData(num, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mHotComicBeanList!![position].comicId)
                startNewActivity(ComicIntroActivity::class.java, mBundle)
            }
            this
        }


        recycler.layoutManager = mLayoutManager
        recycler.addItemDecoration(DividerItemDecoration(mContext!!, mLayoutManager.orientation))
        recycler.adapter = mAdapter


    }

    private fun getHeaderView(hotComicBeanList: List<HotComicBean>, listener: View.OnClickListener) : View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_rank_header_layout, recycler.parent as ViewGroup, false)
        val imageView = view.findViewById<View>(R.id.iv_rank_top) as ImageView
        imageView.loadImg(hotComicBeanList[0].cover)

        val imageView1 = view.findViewById<View>(R.id.iv_rank_sec) as ImageView
        imageView1.loadImg(hotComicBeanList[1].cover)

        val imageView2 = view.findViewById<View>(R.id.iv_rank_thr) as ImageView
        imageView2.loadImg(hotComicBeanList[2].cover)
        imageView.setOnClickListener(listener)
        imageView1.setOnClickListener(listener)
        imageView2.setOnClickListener(listener)
        return view
    }
}