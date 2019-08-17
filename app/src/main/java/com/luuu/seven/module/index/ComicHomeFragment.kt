package com.luuu.seven.module.index

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.module.shelf.ComicCollectFragment
import com.luuu.seven.module.shelf.ComicHistoryFragment
import com.luuu.seven.util.BarUtils
import kotlinx.android.synthetic.main.fra_home_layout.*

class ComicHomeFragment : BaseFragment() {

    private val mTabs = arrayOf("更新", "推荐", "排行")

    companion object {
        fun newInstance(): ComicHomeFragment {
            return ComicHomeFragment()
        }
    }

    override fun initViews() {
        BarUtils.addStatusBarView(status_bg, mContext!!, ContextCompat.getColor(mContext!!, R.color.transparent))
        val fragments = ArrayList<Fragment>()
        val title = ArrayList<String>()
        fragments.add(ComicHistoryFragment())
        fragments.add(ComicIndexFragment())
        fragments.add(ComicCollectFragment())
        title.add(mTabs[0])
        title.add(mTabs[1])
        title.add(mTabs[2])

        initViewPager(title, fragments)
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_home_layout

    private fun initViewPager(names: List<String>, fragments: List<Fragment>) {
        val mAdapter = ComicFragmentAdapter(childFragmentManager, fragments, names)
        home_tabs.setupWithViewPager(home_viewpager.apply {
            adapter = mAdapter
            currentItem = 1
            offscreenPageLimit = 1
        })
    }
}