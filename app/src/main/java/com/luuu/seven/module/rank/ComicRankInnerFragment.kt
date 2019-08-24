package com.luuu.seven.module.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicRankAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.module.index.HomeViewModel
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.*
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicRankInnerFragment : BaseFragment() {

    private lateinit var mViewModel: HomeViewModel
    private var mRankBeanList: ArrayList<HotComicBean> = ArrayList()
    private var mHotComicBeanList: MutableList<HotComicBean>? = null
    private var mHotComicTopList: MutableList<HotComicBean>? = null
    private var mPageNum = 0
    private var pos = 0
    private var num = 0
    private var type = " "
    private lateinit var headerView: View
    private var mAdapter: ComicRankAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    companion object {
        private val Comic_TYPE = "type"

        fun newInstance(type: String): ComicRankInnerFragment {
            val fragment = ComicRankInnerFragment()
            val bundle = Bundle()
            bundle.putString(Comic_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }

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

    override fun onResume() {
        super.onResume()
        mHotComicBeanList = ArrayList()
        mHotComicTopList = ArrayList()
        mPageNum = 0
        mViewModel.getRankComic(num, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHotComicTopList = null
        mHotComicBeanList = null
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
            mViewModel.getRankComic(num, mPageNum)
        }

        mViewModel = obtainViewModel(HomeViewModel::class.java).apply {

            rankData.observe(viewLifecycleOwner, Observer { data ->
                mRankBeanList.addAll(data)

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
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    private fun initAdapter() {
        mAdapter = ComicRankAdapter(R.layout.item_rank_layout, mRankBeanList).apply {
            addHeaderView(headerView)
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mViewModel.getRankComic(num, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mRankBeanList[position].comicId)
                startNewActivity(ComicIntroActivity::class.java, mBundle)
            }
        }

        recycler.layoutManager = mLayoutManager
        recycler.addItemDecoration(DividerItemDecoration(mContext!!, mLayoutManager.orientation))
        recycler.adapter = mAdapter


    }

    private fun getHeaderView(hotComicBeanList: List<HotComicBean>, listener: View.OnClickListener) : View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_rank_header_layout, recycler.parent as ViewGroup, false)
        val imageView = view.findViewById<View>(R.id.iv_rank_top) as ImageView
        imageView.loadWithHead(hotComicBeanList[0].cover)

        val imageView1 = view.findViewById<View>(R.id.iv_rank_sec) as ImageView
        imageView1.loadWithHead(hotComicBeanList[1].cover)

        val imageView2 = view.findViewById<View>(R.id.iv_rank_thr) as ImageView
        imageView2.loadWithHead(hotComicBeanList[2].cover)
        imageView.setOnClickListener(listener)
        imageView1.setOnClickListener(listener)
        imageView2.setOnClickListener(listener)
        return view
    }
}