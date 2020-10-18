package com.luuu.seven.module.intro

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.color
import com.luuu.seven.util.nav
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_EXPANDED
import kotlinx.android.synthetic.main.fra_chapter_layout.*

class ComicChapterFragment : BaseFragment() {

    private val viewModel: IntroViewModel by viewModels({requireParentFragment()})
    private var mAdapter: BaseQuickAdapter<ChapterDataBean, BaseViewHolder>? = null
    private var mHistoryBrowsePosition = 0
    private var mHistoryChapterPosition = 0
    private var mLayoutManager: GridLayoutManager = GridLayoutManager(mContext, 4)
    private var comicId = 0

    override fun initViews() {

        viewModel.comicIntroData.observe(viewLifecycleOwner) { data ->
            data.let {
                comic_name.text = it.title
                updateComicData(it)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lp = chapter_sheet.layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = BarUtils.getStatusBarHeight(requireContext())
        chapter_sheet.layoutParams = lp

        val behavior = BottomSheetBehavior.from(chapter_sheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                super.onSlide(bottomSheet, slideOffset)
            }
        })

        chapter_sheet.doOnLayout {
            val slideOffset = when (behavior.state) {
                STATE_EXPANDED -> 1f
                STATE_COLLAPSED -> 0f
                else /*BottomSheetBehavior.STATE_HIDDEN*/ -> -1f
            }
        }

        recycler_chapter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                view_line.isActivated = recyclerView.canScrollVertically(-1)
            }
        })
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_chapter_layout

    private fun updateComicData(data: ComicIntroBean) {
        mAdapter?.setNewData(data.chapters[0].data) ?: initAdapter(data.chapters[0].data)
//        if (!isBack) {
//            setData(data)
//        }
    }

    fun initAdapter(dataBeanList: ArrayList<ChapterDataBean>) {
        mAdapter = object : BaseQuickAdapter<ChapterDataBean, BaseViewHolder>(
            R.layout.item_chapter_layout, dataBeanList
        ) {

            override fun convert(holder: BaseViewHolder, item: ChapterDataBean?) {

                val data = viewModel.getChapterInfo(holder.absoluteAdapterPosition, item)
                holder.itemView.setBackgroundResource(data.background)
                (holder.itemView as AppCompatTextView).setTextColor(color(data.colorRes))
                mHistoryChapterPosition = data.historyChapterPos
                mHistoryBrowsePosition = data.historyBrowsePos
                holder.setText(R.id.tv_num, data.name)
            }

        }

        recycler_chapter.layoutManager = mLayoutManager
//        mAdapter?.addHeaderView(headerView)
        recycler_chapter.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            viewModel.toRead(position, mHistoryChapterPosition, comicId)
        }
    }

}