package com.luuu.seven.module.index

import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicIndexAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.bean.IndexDataBean
import com.luuu.seven.util.*
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fra_index_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   : 首页
 *     version:
 */
class ComicIndexFragment : BaseFragment() {

    private val mViewModel by viewModels<HomeViewModel>()
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
        appbar_layout.updatePadding(top = paddingTop(requireContext()))

        mViewModel.getHomeData()

        index_refresh.setOnRefreshListener {
            mViewModel.getHomeData()
        }

        appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val percent =
//                (abs(verticalOffset)).toFloat() / (appBarLayout.totalScrollRange).toFloat()
//            toolbar.alpha = percent
//            tv_search_bg.alpha = if (percent >= 0.5) percent else 0.5f
            index_refresh.isEnabled = verticalOffset == 0
            if (verticalOffset == 0) {
                index_banner.startAutoPlay()
            } else {
                index_banner.stopAutoPlay()
            }
        })

        mViewModel.homeList.observe(viewLifecycleOwner) {
            updateIndexList(it)
        }

        mViewModel.homeBanner.observe(viewLifecycleOwner) {
            initPager(it.data)
        }

        mViewModel.toIntro.observeEvent(viewLifecycleOwner) {
            nav().navigate(R.id.action_home_fragment_to_intro_fragment, it)
        }

        mViewModel.toSpecialDetail.observeEvent(viewLifecycleOwner) {
            nav().navigate(R.id.action_home_fragment_to_special_detail_fragment, it)
        }

        mViewModel.toWeb.observeEvent(viewLifecycleOwner) {
            nav().navigate(R.id.action_home_fragment_to_web_fragment, it)
        }

//        tv_search_bg.setOnClickListener { startNewActivity(ComicSearchActivity::class.java) }
//        tv_header_update.setOnClickListener { startNewActivity(ComicUpdateActivity::class.java) }
//        tv_header_rank.setOnClickListener { startNewActivity(ComicRankActivity::class.java) }
//        tv_header_sort.setOnClickListener { startNewActivity(ComicSortActivity::class.java) }
//        tv_header_special.setOnClickListener { startNewActivity(ComicSpecialActivity::class.java) }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_index_layout

    private fun updateIndexList(data: List<IndexBean>) {
        index_refresh.isRefreshing = false
        mAdapter?.setNewData(data) ?: initAdapter(data)
    }

    private fun initAdapter(indexBeanList: List<IndexBean>) {
        mAdapter = ComicIndexAdapter(indexBeanList, mViewModel)
        recycler.layoutManager = LinearLayoutManager(mContext)
        recycler.adapter = mAdapter
    }

    private fun initPager(dataBeanList: List<IndexDataBean>) {
        val urls = dataBeanList.map { it.cover }

        index_banner.apply {
            setIndicatorGravity(BannerConfig.RIGHT)
            //如果url为空则表示是具体的漫画，不然就是广告或者活动
            setOnBannerListener { position ->
                mViewModel.bannerClick(position)
            }
            setImages(urls).setImageLoader(SevenImageLoader()).start()
            setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    mJob = GlobalScope.launch {
                        url2Bitmap(requireContext(), urls[position])?.let {
                            mBitmapGet =
                                Palette.from(it).generate { palette ->
                                    palette?.let {
                                        val mColor = it.getVibrantColor(color(R.color.colorPrimary))
                                        appbar_layout?.setBackgroundColor(mColor)
                                    }
                                }
                        }
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::mJob.isInitialized) {
            mJob.cancel()
        }
        if (this::mBitmapGet.isInitialized) {
            mBitmapGet.cancel(true)
        }
        index_banner.stopAutoPlay()
    }
}