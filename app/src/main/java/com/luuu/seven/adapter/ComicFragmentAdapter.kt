package com.luuu.seven.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   : 首页viewpager适配器
 *     version:
 */
class ComicFragmentAdapter(
    fm: FragmentManager,
    private var fragments: List<Fragment>,
    private var titles: List<String>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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