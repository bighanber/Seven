package com.luuu.seven.module.index

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
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
import com.luuu.seven.util.GlideImageLoader
import com.luuu.seven.util.obtainViewModel
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fra_index_layout.*
import kotlinx.android.synthetic.main.index_header_search_layout.*
import kotlinx.android.synthetic.main.list_header_layout.*
import kotlin.math.abs

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   : 首页
 *     version:
 */
class ComicIndexFragment : BaseFragment() {

    private lateinit var mViewModel: HomeViewModel
    private var mAdapter: ComicIndexAdapter? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
//            BarUtils.setTranslucentForCoordinatorLayout(activity!!, 0)
        }
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
    }


    override fun initViews() {
//        BarUtils.setTranslucentForCoordinatorLayout(activity!!, 0)
        mViewModel = obtainViewModel(HomeViewModel::class.java).apply {
            getHomeData()
        }.apply {
            homeData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    updateIndexList(it)
                }
            })

            dataLoading.observe(viewLifecycleOwner, Observer { isRefresh ->
                isRefresh?.let {
                    index_refresh.isRefreshing = it
                }

            })
        }

        index_refresh.setOnRefreshListener {
            mViewModel.getHomeData()
        }

        appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percent = (abs(verticalOffset)).toFloat() / (appBarLayout.totalScrollRange).toFloat()
            toolbar.alpha = percent
            tv_search_bg.alpha = if (percent >= 0.5) percent else 0.5f
            index_refresh.isEnabled = verticalOffset == 0
            if (verticalOffset == 0) {
                index_banner.startAutoPlay()
            } else {
                index_banner.stopAutoPlay()
            }
        })

        tv_search_bg.setOnClickListener { startNewActivity(ComicSearchActivity::class.java) }
        tv_header_update.setOnClickListener { startNewActivity(ComicUpdateActivity::class.java) }
        tv_header_rank.setOnClickListener { startNewActivity(ComicRankActivity::class.java) }
        tv_header_sort.setOnClickListener { startNewActivity(ComicSortActivity::class.java) }
        tv_header_special.setOnClickListener { startNewActivity(ComicSpecialActivity::class.java) }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_index_layout

    override fun onFirstUserInvisible() {
    }


    private fun updateIndexList(data: List<IndexBean>) {
        initPager(data[0].data)
        //个别栏目不需要就不展示
        val mData = data.filterNot { it.sort == 1 || it.sort == 6 }
        if (mAdapter == null) {
            initAdapter(mData)
        } else {
            mAdapter?.setNewData(mData)
        }
    }

    private fun initAdapter(indexBeanList: List<IndexBean>) {
        val mLayoutManager = LinearLayoutManager(mContext)
        mAdapter = ComicIndexAdapter(indexBeanList)
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
    }

    private fun initPager(dataBeanList: List<IndexDataBean>) {
        index_banner.setIndicatorGravity(BannerConfig.RIGHT)
        val urls = dataBeanList.map { it.cover }

        //如果url为空则表示是具体的漫画，不然就是广告或者活动
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