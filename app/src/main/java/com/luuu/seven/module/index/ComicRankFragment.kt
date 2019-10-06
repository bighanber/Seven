package com.luuu.seven.module.index

import androidx.core.view.updatePadding
import com.luuu.seven.ComicConst
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.util.paddingTop
import kotlinx.android.synthetic.main.fra_inner_viewpager.*

/**
 * 漫画排行
 */
class ComicRankFragment : BaseFragment() {

    private val rankPages = arrayListOf(
        ComicRankInnerFragment.newInstance(ComicConst.RANK_POPULARTY),
        ComicRankInnerFragment.newInstance(ComicConst.RANK_ROAST),
        ComicRankInnerFragment.newInstance(ComicConst.RANK_SUBSCRIBE)
    )

    companion object {
        private val RANK_TITLE = arrayListOf("人气","吐槽","订阅")

        fun newInstance(): ComicRankFragment {
            return ComicRankFragment()
        }
    }

    override fun initViews() {

        sort_layout.updatePadding(top = paddingTop(mContext!!))

        val mAdapter = ComicFragmentAdapter(childFragmentManager, rankPages, RANK_TITLE)

        sort_tabs.setupWithViewPager(sort_viewpager.apply {
            adapter = mAdapter
            currentItem = 0
            offscreenPageLimit = RANK_TITLE.size
        })
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_inner_viewpager


}