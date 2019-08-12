package com.luuu.seven.module.shelf

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicCollectAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.addTo
import com.luuu.seven.util.obtainViewModel
import com.luuu.seven.util.toast
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 收藏界面
 */
class ComicCollectFragment : BaseFragment(){


    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicCollectAdapter? = null

    private lateinit var viewModel: ShelfViewModel

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) {
            viewModel = obtainViewModel().apply {
                getCollect(false).addTo(mSubscription)
            }
            viewModel.collectData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    updateComicCollect(it)
                }
            })
        }
    }

    override fun initViews() {

    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_list_layout

    override fun onFirstUserInvisible() {
    }



    private fun updateComicCollect(data: List<CollectBean>) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter?.setNewData(data)
        }
    }

    private fun initAdapter(collectBeanList: List<CollectBean>) {
        mAdapter = ComicCollectAdapter(R.layout.item_search_layout, collectBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter
        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", collectBeanList[position].comicId)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }

    private fun obtainViewModel(): ShelfViewModel = obtainViewModel(ShelfViewModel::class.java)
}