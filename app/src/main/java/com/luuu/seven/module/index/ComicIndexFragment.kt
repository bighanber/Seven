package com.luuu.seven.module.index

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.adapter.ComicIndexAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.bean.IndexDataBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.GlideImageLoader
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.util.url2Bitmap
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fra_index_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    private lateinit var mJob: Job
    private lateinit var mBitmapGet: AsyncTask<Bitmap, Void, Palette>

    companion object {
        fun newInstance(): ComicIndexFragment {
            return ComicIndexFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        index_banner?.startAutoPlay()
    }

    override fun initViews() {
        val typedValue = TypedValue()
        val mActonBarHeight =
            if (context!!.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
                TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
            else 0
        val mTopPadding = BarUtils.getStatusBarHeight(activity!!) + mActonBarHeight
        appbar_layout.setPadding(0, mTopPadding, 0, 0)

        mViewModel = obtainViewModel(HomeViewModel::class.java).apply {
            getHomeData()

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
            val percent =
                (abs(verticalOffset)).toFloat() / (appBarLayout.totalScrollRange).toFloat()
//            toolbar.alpha = percent
//            tv_search_bg.alpha = if (percent >= 0.5) percent else 0.5f
            index_refresh.isEnabled = verticalOffset == 0
            if (verticalOffset == 0) {
                index_banner.startAutoPlay()
            } else {
                index_banner.stopAutoPlay()
            }
        })

//        tv_search_bg.setOnClickListener { startNewActivity(ComicSearchActivity::class.java) }
//        tv_header_update.setOnClickListener { startNewActivity(ComicUpdateActivity::class.java) }
//        tv_header_rank.setOnClickListener { startNewActivity(ComicRankActivity::class.java) }
//        tv_header_sort.setOnClickListener { startNewActivity(ComicSortActivity::class.java) }
//        tv_header_special.setOnClickListener { startNewActivity(ComicSpecialActivity::class.java) }
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
        index_banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mJob = GlobalScope.launch {
                    mBitmapGet =
                        Palette.from(url2Bitmap(mContext!!, urls[position])).generate { palette ->
                            palette?.let {
                                val mColor = it.getVibrantColor(
                                    ContextCompat.getColor(
                                        mContext!!,
                                        R.color.colorPrimary
                                    )
                                )
                                appbar_layout?.setBackgroundColor(mColor)
                            }
                        }
                }
            }

        })

    }

    override fun onPause() {
        super.onPause()
        if (this::mJob.isInitialized) {
            mJob.cancel()
            mBitmapGet.cancel(true)
        }
        index_banner.stopAutoPlay()
    }
}