package com.luuu.seven.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.luuu.seven.module.read.ComicLoadingFragment
import com.luuu.seven.module.read.ComicReadFragment

/**
 * Created by lls on 2017/8/7.
 *
 */
class ComicReadPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var mUrls: MutableList<String> = mutableListOf()
    private var mBytes: MutableList<ByteArray> = mutableListOf()

    private var count = 1
    private var isFromDisk = false

    override fun getItem(position: Int): Fragment {
        if (position == 0 || position == count + 1) {
            return ComicLoadingFragment.newInstance(position)
        }
        if (mUrls.isEmpty() && mBytes.isEmpty()) {
            return ComicReadFragment.newInstance("nodata", null, isFromDisk)
        }
        return ComicReadFragment.newInstance(if (isFromDisk)  null else mUrls[position - 1],
                if (isFromDisk) mBytes[position - 1] else null, isFromDisk)
    }

    override fun getCount(): Int {
        return count + 2
    }

    override fun getPageWidth(position: Int): Float {
        if (position == 0 || position == count + 1) {
            return 0.2f
        }
        return super.getPageWidth(position)
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }

    fun replaceAll(urls: MutableList<String>, bytes: MutableList<ByteArray>?, isFromDisk: Boolean) {
        this.isFromDisk = isFromDisk

        if (isFromDisk) {
            mBytes.clear()
            mBytes.addAll(bytes!!)
            count = bytes.size
        } else {
            mUrls.clear()
            mUrls.addAll(urls)
            count = urls.size
        }

        notifyDataSetChanged()
    }
}