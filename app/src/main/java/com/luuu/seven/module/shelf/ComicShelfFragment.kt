package com.luuu.seven.module.shelf

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import kotlinx.android.synthetic.main.fra_shelf_layout.*


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   : 书架
 *     version:
 */
class ComicShelfFragment : BaseFragment() {


    private val mTabs = arrayOf("历史", "收藏")

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
//            BarUtils.setColorForToolbarInFragment(activity!!,
//                    ContextCompat.getColor(mContext!!, R.color.colorAccent), shelf_tabs)
        }
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
    }

    override fun initViews() {
//        BarUtils.setColorForToolbarInFragment(activity!!,
//                ContextCompat.getColor(mContext!!, R.color.colorAccent), shelf_tabs)
        val fragments = ArrayList<Fragment>()
        val title = ArrayList<String>()
        fragments.add(ComicHistoryFragment())
        fragments.add(ComicCollectFragment())
        title.add(mTabs[0])
        title.add(mTabs[1])

        initViewPager(title, fragments)
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_layout

    override fun onFirstUserInvisible() {
    }

    private fun initViewPager(names: List<String>, fragments: List<Fragment>) {
        val adapter = ComicFragmentAdapter(childFragmentManager, fragments, names)
        shelf_viewpager.adapter = adapter
        shelf_viewpager.setCurrentItem(0, false)
        shelf_viewpager.offscreenPageLimit = 1
        shelf_tabs.setupWithViewPager(shelf_viewpager)
        shelf_tabs.setScrollPosition(0, 0f, true)
    }
}