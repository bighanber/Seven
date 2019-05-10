package com.luuu.seven.module.news

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.luuu.seven.R
import com.luuu.seven.WebActivity
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.GlideImageLoader
import com.luuu.seven.util.addTo
import com.luuu.seven.util.obtainViewModel
import kotlinx.android.synthetic.main.fra_news_layout.*

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:资讯
 */
class ComicNewsFragment : BaseFragment() {

    private lateinit var fm: FragmentManager
    private var mNewsListFragment: ComicNewsListFragment? = null
    private var mNewsFlashFragment: ComicNewsFlashFragment? = null

    private lateinit var viewModel: NewsViewModel

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            BarUtils.setTranslucentForCoordinatorLayout(activity!!, 0)
        }
    }

    override fun initViews() {
        BarUtils.setTranslucentForCoordinatorLayout(activity!!, 0)

        viewModel = obtainViewModel().apply {
            getComicNewsPic(false).addTo(mSubscription)
        }.apply {
            newsPicData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    updateComicList(it)
                }
            })
        }

        fm = childFragmentManager
        showFragment(0)
        rg_group.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_news -> showFragment(0)
                R.id.rb_flash -> showFragment(1)
            }
        }
        appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                news_banner.startAutoPlay()
            } else {
                news_banner.stopAutoPlay()
            }
                })
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_news_layout

    override fun onFirstUserInvisible() {
    }


    private fun updateComicList(data: ComicNewsPicBean) {
        val urls = ArrayList<String>()
        data.data.mapTo(urls) { it.picUrl }
        news_banner.setOnBannerListener{ position ->
            val mBundle = Bundle()
            mBundle.putString("url", data.data[position].objectUrl)
            startNewActivity(WebActivity::class.java, mBundle)
        }
        news_banner.setImages(urls).setImageLoader(GlideImageLoader()).start()
    }

    private fun showFragment(id: Int) {
        val ft = fm.beginTransaction()
        hideAllFragment(ft)
        when (id) {
            0 -> if (mNewsListFragment != null) {
                ft.show(mNewsListFragment!!)
            } else {
                mNewsListFragment = ComicNewsListFragment()
                ft.add(R.id.content, mNewsListFragment!!)
            }
            1 -> if (mNewsFlashFragment != null) {
                ft.show(mNewsFlashFragment!!)
            } else {
                mNewsFlashFragment = ComicNewsFlashFragment()
                ft.add(R.id.content, mNewsFlashFragment!!)
            }
        }
        ft.commit()
    }

    private fun hideAllFragment(ft: FragmentTransaction) {
        if (mNewsListFragment != null) ft.hide(mNewsListFragment!!)
        if (mNewsFlashFragment != null) ft.hide(mNewsFlashFragment!!)
    }

    private fun obtainViewModel(): NewsViewModel = obtainViewModel(NewsViewModel::class.java)
}