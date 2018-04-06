package com.luuu.seven.module.index

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.adapter.ComicIndexAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.bean.IndexDataBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.module.rank.ComicRankActivity
import com.luuu.seven.module.search.ComicSearchActivity
import com.luuu.seven.module.sort.ComicSortActivity
import com.luuu.seven.module.special.ComicSpecialActivity
import com.luuu.seven.module.update.ComicUpdateActivity
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.GlideImageLoader
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fra_index_layout.*
import kotlinx.android.synthetic.main.index_header_search_layout.*
import kotlinx.android.synthetic.main.list_header_layout.*

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:首页
 */
class ComicIndexFragment : BaseFragment(), ComicIndexContract.View {

    private var mAdapter: ComicIndexAdapter? = null
    private val mPresent by lazy { ComicIndexPresenter(this) }

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            BarUtils.setTranslucentForCoordinatorLayout(activity, 0)
        }
    }

    override fun initViews() {
        BarUtils.setTranslucentForCoordinatorLayout(activity, 0)
        mPresent.getIndexData()
        index_refresh.setOnRefreshListener {
            mPresent.refreshData()
        }
        appbar_layout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = (Math.abs(verticalOffset)).toFloat() / (appBarLayout.totalScrollRange).toFloat()
            toolbar.alpha = percent
            tv_search_bg.alpha = if (percent >= 0.5) percent else 0.5f
            index_refresh.isEnabled = verticalOffset == 0
            if (verticalOffset == 0) {
                index_banner.startAutoPlay()
            } else {
                index_banner.stopAutoPlay()
            }
        }

        tv_search_bg.setOnClickListener{ startNewActivity(ComicSearchActivity::class.java) }
        tv_header_update.setOnClickListener { startNewActivity(ComicUpdateActivity::class.java) }
        tv_header_rank.setOnClickListener { startNewActivity(ComicRankActivity::class.java) }
        tv_header_sort.setOnClickListener { startNewActivity(ComicSortActivity::class.java) }
        tv_header_special.setOnClickListener { startNewActivity(ComicSpecialActivity::class.java) }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_index_layout

    override fun onFirstUserInvisible() {
    }

    override fun onPause() {
        super.onPause()
        mPresent.unsubscribe()
    }


    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateIndexList(data: List<IndexBean>) {
        initPager(data[0].data)
        val mData = data.filterNot { it.sort == 1 || it.sort == 6 }
        if (mAdapter == null) {
            initAdapter(mData)
        } else {
            mAdapter!!.setNewData(mData)
        }
    }

    override fun judgeRefresh(isRefresh: Boolean) {
        index_refresh.isRefreshing = isRefresh
    }

    private fun initAdapter(indexBeanList: List<IndexBean>) {
        val mLayoutManager = LinearLayoutManager(mContext)
        mAdapter = ComicIndexAdapter(R.layout.list_index_item_layout, indexBeanList)
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
    }

    private fun initPager(dataBeanList: List<IndexDataBean>) {
        index_banner.setIndicatorGravity(BannerConfig.RIGHT)
        val urls = dataBeanList.map { it.cover }
        index_banner.setOnBannerListener { position ->
            if ("" == dataBeanList[position].url) {
                val mBundle = Bundle()
                mBundle.putInt("comicId", dataBeanList[position].objId)
                startNewActivity(ComicIntroActivity::class.java, mBundle)
            } else {
                val mBundle = Bundle()
                mBundle.putString("url", dataBeanList[position].url)
                startNewActivity(WebActivity::class.java, mBundle)
            }
        }
        index_banner.setImages(urls).setImageLoader(GlideImageLoader()).start()
    }
}