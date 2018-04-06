package com.luuu.seven.module.read

import android.os.Bundle
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import kotlinx.android.synthetic.main.fra_loading_layout.*


/**
 * Created by lls on 2017/8/7.
 * 上下一页加载界面
 */
class ComicLoadingFragment : BaseFragment() {

    private var pos = 0

    companion object {
        fun newInstance(position: Int): ComicLoadingFragment {
            val fragment = ComicLoadingFragment()
            val args = Bundle()
            args.putInt("position", position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun initViews() {
        arguments?.let {
            pos = it.getInt("position")
            if (pos == 0) tv_loading.text = "加载上一话"
            else tv_loading.text = "加载下一话"
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_loading_layout
    override fun onFirstUserInvisible() {
    }
}