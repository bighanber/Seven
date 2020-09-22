package com.luuu.seven.module.shelf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicCollectAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.CollectBean
import kotlinx.android.synthetic.main.fra_shelf_list_layout.*

/**
 * Created by lls on 2017/8/9.
 * 收藏界面
 */
class ComicCollectFragment : BaseFragment() {


    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }
    private var mAdapter: ComicCollectAdapter? = null

    private val viewModel: ShelfViewModel by viewModels()

    override fun initViews() {
        viewModel.collectData.observe(viewLifecycleOwner) { data ->
            data?.let {
                updateComicCollect(it)
            }
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_shelf_list_layout

    override fun onStart() {
        super.onStart()
        Log.e("asd", "coll - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd", "coll - onResume")
        viewModel.getCollect()
    }


    private fun updateComicCollect(data: List<CollectBean>) {

        mAdapter?.setNewData(data) ?: initAdapter(data)
    }

    private fun initAdapter(collectBeanList: List<CollectBean>) {
        mAdapter = ComicCollectAdapter(R.layout.item_search_layout, collectBeanList)
        recycler_shelf.layoutManager = mLayoutManager
        recycler_shelf.adapter = mAdapter
        mAdapter?.setOnItemClickListener { _, _, position ->
//            startActivity<ComicIntroActivity>(ComicIntroActivity.COMIC_ID to collectBeanList[position].comicId)
            findNavController().navigate(
                R.id.action_home_fragment_to_intro_fragment,
                Bundle().apply { putInt("comicId", collectBeanList[position].comicId) })
        }
    }
}