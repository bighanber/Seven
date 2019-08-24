package com.luuu.seven.module.index

import android.util.TypedValue
import androidx.fragment.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.module.rank.ComicRankInnerFragment
import com.luuu.seven.util.BarUtils
import kotlinx.android.synthetic.main.fra_inner_viewpager.*

/**
 * 漫画排行
 */
class ComicRankFragment : BaseFragment() {

    private val mTabs = arrayOf("人气","吐槽","订阅")

    companion object {
        fun newInstance(): ComicRankFragment {
            return ComicRankFragment()
        }
    }

    override fun initViews() {

        val typedValue = TypedValue()
        val mActonBarHeight = if (context!!.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true))
            TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
        else 0
        val mTopPadding = BarUtils.getStatusBarHeight(activity!!) + mActonBarHeight
        sort_layout.setPadding(0, mTopPadding, 0, 0)

        val fragments = ArrayList<Fragment>()
        val title = ArrayList<String>()
        for (i in mTabs.indices) {
            fragments.add(ComicRankInnerFragment.newInstance(mTabs[i]))
            title.add(mTabs[i])
        }
        initViewPager(title, fragments)
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_inner_viewpager

    private fun initViewPager(names: List<String>, fragments: List<Fragment>) {
        val mAdapter = ComicFragmentAdapter(childFragmentManager, fragments, names)

        sort_tabs.setupWithViewPager(sort_viewpager.apply {
            adapter = mAdapter
            currentItem = 0
            offscreenPageLimit = 1
        })
    }

}