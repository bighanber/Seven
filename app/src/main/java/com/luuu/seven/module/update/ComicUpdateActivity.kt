package com.luuu.seven.module.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.module.index.ComicUpdateFragment
import kotlinx.android.synthetic.main.activity_comic_tab.*


/***
 * 最近更新界面
 *
 */
class ComicUpdateActivity : BaseActivity() {

    private val mTabs = arrayOf("全部", "原创", "译制")

    override fun initViews() {
//        setToolbarTitle(" ")
//        val fragments = ArrayList<Fragment>()
//        val title = ArrayList<String>()
//        for (i in mTabs.indices) {
//            fragments.add(ComicUpdateFragment.newInstance(mTabs[i]))
//            title.add(mTabs[i])
//        }
//        initViewPager(title, fragments)
    }

    override fun getIntentExtras(extras: Bundle?) {
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_tab

    private fun initViewPager(names: List<String>, fragments: List<Fragment>) {
            val adapter = ComicFragmentAdapter(supportFragmentManager,
                    fragments, names)
        viewpager.adapter = adapter
        viewpager.setCurrentItem(0, false)
        viewpager.offscreenPageLimit = 1
        tabs.setupWithViewPager(viewpager)
        tabs.setScrollPosition(0, 0f, true)
    }
}
