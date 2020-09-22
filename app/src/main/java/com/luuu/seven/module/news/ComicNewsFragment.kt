package com.luuu.seven.module.news

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.util.SevenImageLoader
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

    private val viewModel: NewsViewModel by viewModels()

    override fun initViews() {

        viewModel.apply {
            getComicNewsPic()

            newsPicData.observe(viewLifecycleOwner) { data ->
                data?.let {
                    updateComicList(it)
                }
            }
        }

        fm = childFragmentManager
        showFragment(0)
        rg_group.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rb_news -> showFragment(0)
                R.id.rb_flash -> showFragment(1)
            }
        }
        appbar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) {
                news_banner.startAutoPlay()
            } else {
                news_banner.stopAutoPlay()
            }
        })
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_news_layout

    private fun updateComicList(data: ComicNewsPicBean) {
        val urls = ArrayList<String>()
        data.data.mapTo(urls) { it.picUrl }
        news_banner.setOnBannerListener { position ->
//            startActivity<WebActivity>("url" to data.data[position].objectUrl)
            findNavController().navigate(R.id.action_home_fragment_to_web_fragment, Bundle().apply { putString("url", data.data[position].objectUrl) })
        }
        news_banner.setImages(urls).setImageLoader(SevenImageLoader()).start()
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

}