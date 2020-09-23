package com.luuu.seven.module.special

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicSpecialAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicSpecialBean
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fra_comic_special.*

/***
 * 漫画专题
 *
 */
class ComicSpecialFragment : BaseFragment() {

    private var mAdapter: ComicSpecialAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(requireContext()) }
    private var mSpecialBeanList: MutableList<ComicSpecialBean>? = null
    private var mPageNum = 0
    private val specialViewModel by viewModels<SpecialViewModel>()

    override fun initViews() {
//        toolbar_title?.text = "漫画专题"
        mSpecialBeanList = ArrayList()
        refresh_special.setOnRefreshListener {
            mPageNum = 0
            specialViewModel.getSpecialComic(mPageNum)
        }
        specialViewModel.getSpecialComic(0)

        specialViewModel.specialData.observe(this) {
            updateComicList(it)
        }
    }

    //    override fun initViews() {
//        toolbar_title?.text = "漫画专题"
//
//        mSpecialBeanList = ArrayList()
//
//        refresh_special.setOnRefreshListener {
//            mPageNum = 0
//            specialViewModel.getSpecialComic(mPageNum)
//        }
//        specialViewModel.getSpecialComic(0)
//
//        specialViewModel.specialData.observe(this) {
//            updateComicList(it)
//        }
//    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_comic_special

    override fun onDestroy() {
        super.onDestroy()
        mSpecialBeanList = null
    }

    private fun updateComicList(data: List<ComicSpecialBean>) {
        if (mPageNum > 0) {
            if (data.isEmpty()) {
                showToast("没有数据咯")
                mAdapter?.loadMoreEnd()
            } else {
                mAdapter?.addData(data)
                mAdapter?.loadMoreComplete()
                mSpecialBeanList?.addAll(data)
            }
        } else {
            mSpecialBeanList = data.toMutableList()
            mAdapter?.setNewData(data) ?: initAdapter(data)
        }
    }

    private fun initAdapter(specialListBeanList: List<ComicSpecialBean>) {
        mAdapter = ComicSpecialAdapter(R.layout.item_special_layout, specialListBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                specialViewModel.getSpecialComic(mPageNum)
            }, special_recycler)
            setOnItemClickListener { _, _, position ->
                if (mSpecialBeanList!![position].pageType == 1) {
                    val mBundle = Bundle()
                    mBundle.putString("url", mSpecialBeanList!![position].pageUrl)
//                    startNewActivity(WebActivity::class.java, mBundle)
                    findNavController().navigate(R.id.action_special_fragment_to_web_fragment, mBundle)
                } else {
                    val mBundle = Bundle()
                    mBundle.putInt("tagId", mSpecialBeanList!![position].id)
                    mBundle.putString("title", mSpecialBeanList!![position].title)
//                    startNewActivity(ComicSpecialDetailActivity::class.java, mBundle)
                    findNavController().navigate(R.id.action_special_fragment_to_special_detail_fragment, mBundle)
                }
            }
        }
        special_recycler.layoutManager = mLayoutManager
        special_recycler.adapter = mAdapter
    }
}
