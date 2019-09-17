package com.luuu.seven.module.index

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.module.shelf.ComicCollectFragment
import com.luuu.seven.module.shelf.ComicHistoryFragment
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.pageChangeListener
import kotlinx.android.synthetic.main.fra_home_layout.*

class ComicHomeFragment : BaseFragment() {

    private val homePages = arrayListOf(
        ComicUpdateFragment.newInstance(),
        ComicIndexFragment.newInstance(),
        ComicRankFragment.newInstance()
    )

    companion object {
        private val HOME_TITLES = arrayListOf("更新", "推荐", "排行")

        fun newInstance(): ComicHomeFragment {
            return ComicHomeFragment()
        }
    }

    override fun initViews() {
        BarUtils.addStatusBarView(status_bg, mContext!!, ContextCompat.getColor(mContext!!, R.color.transparent))

        val mAdapter = ComicFragmentAdapter(childFragmentManager, homePages, HOME_TITLES)
        home_tabs.setupWithViewPager(home_viewpager.apply {
            adapter = mAdapter
            currentItem = 1
            offscreenPageLimit = homePages.size
        })
        home_viewpager.pageChangeListener {
            onPageSelected {  pos ->
                if (pos != 1) {
                    tool_bg.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.colorPrimary))
                    status_bg.setBackgroundColor(ContextCompat.getColor(mContext!!, R.color.colorPrimary))
                }
            }
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_home_layout
}