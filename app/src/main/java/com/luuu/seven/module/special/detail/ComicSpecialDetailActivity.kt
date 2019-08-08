package com.luuu.seven.module.special.detail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicSpecialDetailAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ComicSpecialDetBean
import com.luuu.seven.module.intro.ComicIntroActivity
import kotlinx.android.synthetic.main.activity_comic_special_detail.*

/**
 * Created by lls on 2017/8/9.
 * 专题详情界面
 */
class ComicSpecialDetailActivity : BaseActivity(), ComicSpecialDetailContract.View{

    private val mPresent by lazy { ComicSpecialDetailPresenter(this) }
    private var mAdapter: ComicSpecialDetailAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private var tagId = 0
    private var mTitle = ""

    override fun initViews() {
        setToolbarTitle(mTitle)
        mPresent.getComicSpecialDetail(tagId)
    }

    override fun getIntentExtras(extras: Bundle?) {
        tagId = extras?.getInt("tagId") ?: 0
        mTitle = extras?.getString("title") ?: " "
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_special_detail

    override fun onDestroy() {
        super.onDestroy()
        mPresent.unsubscribe()
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun updateComicList(data: ComicSpecialDetBean) {
        if (mAdapter == null) {
            initAdapter(data)
        } else {
            mAdapter!!.setNewData(data.comics)
        }
    }

    private fun initAdapter(specialDetBean: ComicSpecialDetBean) {
        mAdapter = ComicSpecialDetailAdapter(R.layout.item_special_det_layout, specialDetBean.comics)
        recycler_special_det.layoutManager = mLayoutManager
        recycler_special_det.adapter = mAdapter

        mAdapter!!.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", specialDetBean.comics[position].id)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}
