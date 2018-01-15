package com.luuu.seven.adapter

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ComicFragmentAdapter(fm: FragmentManager, private var fragments: List<Fragment>, private var titles: List<String>) : FragmentPagerAdapter(fm) {

//    private var mFragmentManager: FragmentManager = fm
//    private var mFragments: List<BaseFragment> = fragments
//    private var mTitles: List<String> = titles

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

}