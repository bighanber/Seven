package com.luuu.seven.module.rank

import android.os.Bundle
import android.support.v4.app.Fragment
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseActivity
import kotlinx.android.synthetic.main.activity_comic_tab.*

/**
 * Created by lls on 2017/8/9.
 *漫画排行
 */
class ComicRankActivity : BaseActivity() {

    private val mTabs = arrayOf("人气","吐槽","订阅")

    override fun initViews() {
        setToolbarTitle(" ")
        val fragments = ArrayList<Fragment>()
        val title = ArrayList<String>()
        for (i in mTabs.indices) {
            fragments.add(ComicRankFragment.newInstance(mTabs[i]))
            title.add(mTabs[i])
        }
        initViewPager(title, fragments)
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
