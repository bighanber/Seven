package com.luuu.seven.module.special

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicSpecialDetailAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicSpecialDetBean
import com.luuu.seven.util.nav
import kotlinx.android.synthetic.main.fra_comic_special_detail.*

/**
 * Created by lls on 2017/8/9.
 * 专题详情界面
 */
class ComicSpecialDetailFragment : BaseFragment(){

    private var mAdapter: ComicSpecialDetailAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(requireContext()) }
    private var tagId = 0
    private var mTitle = ""
    private val specialViewModel by viewModels<SpecialViewModel>()

    override fun initViews() {

        tagId = arguments?.getInt("tagId") ?: 0
        mTitle = arguments?.getString("title") ?: " "

        setToolbarTitle(mTitle)
        specialViewModel.getSpecialComicDetail(tagId)
        specialViewModel.specialDetail.observe(this) {
            mAdapter?.setNewData(it.comics) ?: initAdapter(it)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_comic_special_detail

    private fun initAdapter(specialDetBean: ComicSpecialDetBean) {
        mAdapter = ComicSpecialDetailAdapter(R.layout.item_special_det_layout, specialDetBean.comics)
        recycler_special_det.layoutManager = mLayoutManager
        recycler_special_det.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", specialDetBean.comics[position].id)
            nav().navigate(
                R.id.action_special_detail_fragment_to_intro_fragment,mBundle)
//            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}
