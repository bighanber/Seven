package com.luuu.seven.module.intro

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.util.*
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_EXPANDED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_HIDDEN
import kotlinx.android.synthetic.main.fra_comic_intro.*
import kotlinx.android.synthetic.main.include_comic_intro.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   : 漫画详情界面
 *     version:
 */
class ComicIntroFragment : BaseFragment() {

    private val viewModel: IntroViewModel by viewModels()
    private val mLayoutManager: GridLayoutManager by lazy { GridLayoutManager(requireContext(), 4) }
    private var mAdapter: BaseQuickAdapter<ChapterDataBean, BaseViewHolder>? = null
//    private lateinit var mComicIntroBean: ComicIntroBean
    private var comicId = 0
    private var mHistoryBrowsePosition = 0
    private var mHistoryChapterPosition = 0
    private var isBack = false

    private var comicTitle = ""
    private var comicCover = ""
//    private var mFilterChapterList: List<ChapterDataBean>? = null
    private var mChapterList: ArrayList<ChapterDataBean> = ArrayList()
//    private var isMoreThanEight = false

//    private var readHistoryData: ReadHistoryBean? = null
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(requireView().findViewById(R.id.chapter_sheet)) }

    companion object {
        const val COMIC_ID = "comicId"
    }

    override fun initViews() {

        BarUtils.setPaddingSmart(requireContext(), toolbar)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == STATE_HIDDEN) {
                    start_read_fab.show()
                } else {
                    start_read_fab.hide()
                }
            }
        })
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.skipCollapsed = true
        if (bottomSheetBehavior.state == STATE_COLLAPSED) {
            bottomSheetBehavior.state = STATE_HIDDEN
        }

        comicId = arguments?.getInt(COMIC_ID) ?: 0

        viewModel.getReadHistory(comicId)
        viewModel.isFavorite(comicId)

        comic_list.click {
            bottomSheetBehavior.state = STATE_EXPANDED
        }

        comic_follow.click {
            if (comic_follow.isSelected) {
                viewModel.unFavoriteComic(comicId)
            } else {
                viewModel.favoriteComic(comicId, comicTitle, tv_intro_authors.text.toString(), comicCover, Date().time)
            }
        }

        toolbar.click {
            nav().popBackStack()
        }

        viewModel.comicIntroData.observe(this) { data ->
            if (!isBack) {
                setData(data)
            }
        }

        viewModel.comicIntroFilter.observe(this) {
            mChapterList.addAll(it)
            mAdapter?.notifyDataSetChanged() ?: initAdapter()
        }

        viewModel.updateFavorite.observe(this) {
            comic_follow.isSelected = it
        }

        viewModel.readHistory.observe(this) {
//            readHistoryData = it.getOrNull(0)
            viewModel.getComicIntro(comicId)
            viewModel.getComicRelated(comicId)
        }

        viewModel.comicRelatedData.observe(this){

        }

        viewModel.bottomSheetExpand.observe(this) {
            bottomSheetBehavior.state = STATE_EXPANDED
        }

        viewModel.toReadPage.observeEvent(this) {
            nav().navigate(R.id.action_intro_fragment_to_read_fragment, it)
//            startNewActivityForResult(ComicReadRecyclerActivity::class.java, 10002, mBundle)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_comic_intro


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10002) {
            isBack = true
            viewModel.getComicIntro(comicId)
        }
    }

    fun initAdapter() {
        mAdapter = object : BaseQuickAdapter<ChapterDataBean, BaseViewHolder>(
            R.layout.item_chapter_layout, mChapterList
        ) {

            override fun convert(holder: BaseViewHolder, item: ChapterDataBean?) {
                val data = viewModel.getChapterInfo(holder.absoluteAdapterPosition, item)
                holder.setText(R.id.tv_num, data.name)
                holder.itemView.setBackgroundResource(data.background)
                (holder.itemView as AppCompatTextView).setTextColor(color(data.colorRes))
                mHistoryChapterPosition = data.historyChapterPos
                mHistoryBrowsePosition = data.historyBrowsePos
            }
        }

        chapter_grid.layoutManager = mLayoutManager
        chapter_grid.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            viewModel.toRead(position, mHistoryChapterPosition, comicId)
        }
    }

    private fun setData(data: ComicIntroBean) {

        collap.title = data.title
        tv_intro_title.text = data.title

        tv_intro_authors.text =
            string(R.string.intro_comic_authors,
                data.authors.joinToString(separator = " ") { it.tagName })
        tv_intro_tags.text =
            string(R.string.intro_comic_types,
                data.types.joinToString(separator = " ") { it.tagName })

        comic_last_update.text =
            string(R.string.intro_comic_last_update, (data.lastUpdatetime * 1000).toDateString())
        comic_introduction.text = data.description
        iv_cha_img.loadBlur(data.cover)
        comic_img.loadWithHead(data.cover)

        comicTitle = data.title
        comicCover = data.cover
    }

}
