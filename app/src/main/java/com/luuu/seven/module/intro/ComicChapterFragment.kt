package com.luuu.seven.module.intro

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.module.read.recycler.ComicReadRecyclerActivity
import com.luuu.seven.util.*
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_EXPANDED
import kotlinx.android.synthetic.main.fra_chapter_layout.*

class ComicChapterFragment : BaseFragment() {

    private lateinit var viewModel: IntroViewModel
    private var mAdapter: BaseQuickAdapter<ChapterDataBean, BaseViewHolder>? = null
    private lateinit var mComicIntroBean: ComicIntroBean
    private var isBack = false
    private var mHistoryBrowsePosition = 0
    private var mHistoryChapterPosition = 0
    private var mLayoutManager: GridLayoutManager = GridLayoutManager(mContext, 4)
    private var comicId = 0

    override fun initViews() {

        viewModel = activityViewModel<IntroViewModel>().apply {
            comicIntroData.observe(viewLifecycleOwner, Observer { data ->
                data?.let {
                    comic_name.text = it.title
                    updateComicData(it)
                }
            })
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lp = chapter_sheet.layoutParams as ViewGroup.MarginLayoutParams
        lp.topMargin = BarUtils.getStatusBarHeight(context!!)
        chapter_sheet.layoutParams = lp

        val behavior = BottomSheetBehavior.from(chapter_sheet)
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                super.onSlide(bottomSheet, slideOffset)
            }
        })

        chapter_sheet.doOnLayout {
            val slideOffset = when(behavior.state) {
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
        if (mAdapter == null) {
            initAdapter(data.chapters[0].data)
        } else {
            mAdapter?.setNewData(data.chapters[0].data)
        }
        mComicIntroBean = data
//        if (!isBack) {
//            setData(data)
//        }
    }

    fun initAdapter(dataBeanList: ArrayList<ChapterDataBean>) {
        mAdapter = object : BaseQuickAdapter<ChapterDataBean, BaseViewHolder>(R.layout.item_chapter_layout, dataBeanList) {

            override fun convert(holder: BaseViewHolder, item: ChapterDataBean?) {
                ifNotNull(holder, item) { holder, item ->
                    holder.setText(R.id.tv_num, item.chapterTitle)
                    if (mComicIntroBean.mReadHistoryBean != null && item.chapterId == mComicIntroBean.mReadHistoryBean!!.chapterId) {
                        holder.itemView.setBackgroundResource(R.drawable.chapter_read_backgroud)
                        (holder.itemView as AppCompatTextView).setTextColor(0xFFFFFFFF.toInt())
                        mHistoryChapterPosition = holder.adapterPosition
                        mHistoryBrowsePosition = if (mComicIntroBean.mReadHistoryBean!!.browsePosition < 1)
                            1
                        else
                            mComicIntroBean.mReadHistoryBean!!.browsePosition
                    } else {
                        holder.itemView.setBackgroundResource(R.drawable.chapter_backgroud)
                        (holder.itemView as AppCompatTextView).setTextColor(ContextCompat.getColor(mContext, R.color.content))
                    }
                }
            }

        }

        recycler_chapter.layoutManager = mLayoutManager
//        mAdapter?.addHeaderView(headerView)
        recycler_chapter.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.run {
                putInt("comicId", comicId)
                putParcelableArrayList("comicChapter", dataBeanList)
                putInt("comicPosition", position)
                putString("comicTagName", dataBeanList[position].chapterTitle)
                putString("comicCover", mComicIntroBean.cover)
                putString("comicTitle", mComicIntroBean.title)
                putInt("historyPosition", if (position + 1 == mHistoryChapterPosition) mHistoryBrowsePosition else 0)
            }
//            startNewActivityForResult(ComicReadRecyclerActivity::class.java, 10002, mBundle)
        }
    }

}