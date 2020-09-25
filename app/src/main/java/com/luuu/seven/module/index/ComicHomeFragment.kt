package com.luuu.seven.module.index

import androidx.navigation.fragment.findNavController
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.util.*
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
        BarUtils.addStatusBarView(status_bg, requireContext(), color(R.color.transparent))

        tv_search.click {
            nav().navigate(R.id.action_home_pager_fragment_to_search_fragment)
        }

        iv_history.click {
//            startActivity<ComicSortActivity>()
            nav().navigate(R.id.action_home_fragment_to_sort_fragment)
        }

        val mAdapter = ComicFragmentAdapter(childFragmentManager, homePages, HOME_TITLES)
        home_tabs.setupWithViewPager(home_viewpager.apply {
            adapter = mAdapter
            currentItem = 1
            offscreenPageLimit = homePages.size
        })

        home_viewpager.pageChangeListener {
            onPageSelected {  pos ->
                if (pos != 1) {
                    tool_bg.setBackgroundColor(color(R.color.colorPrimary))
                    status_bg.setBackgroundColor(color(R.color.colorPrimary))
                } else {
                    tool_bg.setBackgroundColor(color(R.color.transparent))
                    status_bg.setBackgroundColor(color(R.color.transparent))
                }
            }
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_home_layout
}