package com.luuu.seven

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.module.index.ComicHomeFragment
import com.luuu.seven.module.news.ComicNewsFragment
import com.luuu.seven.module.shelf.ComicShelfFragment
import kotlinx.android.synthetic.main.fra_main.*

class MainFragment : BaseFragment() {

    private val fragmentList = arrayListOf<Fragment>()
    private val indexFragment: ComicHomeFragment by lazy { ComicHomeFragment.newInstance() }
    private val shelfFragment: ComicShelfFragment by lazy { ComicShelfFragment() }
    private val newsFragment: ComicNewsFragment by lazy { ComicNewsFragment() }

    init {
        fragmentList.apply {
            add(indexFragment)
            add(shelfFragment)
            add(newsFragment)
        }
    }

    override fun initViews() {
        main_page.adapter = ComicFragmentAdapter(childFragmentManager, fragmentList, arrayListOf("1","2","3"))
        main_page.offscreenPageLimit = fragmentList.size

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_index -> {
                    if (item.isChecked) return@setOnNavigationItemSelectedListener true
                    item.isChecked = true
                    main_page.currentItem = 0
                }
                R.id.navigation_shelf -> {
                    if (item.isChecked) return@setOnNavigationItemSelectedListener true
                    item.isChecked = true
                    main_page.currentItem = 1
                }
                R.id.navigation_news -> {
                    if (item.isChecked) return@setOnNavigationItemSelectedListener true
                    item.isChecked = true
                    main_page.currentItem = 2
                }
            }
            true
        }
    }

    override fun getContentViewLayoutID(): Int  = R.layout.fra_main

}