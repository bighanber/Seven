package com.luuu.seven.module.intro

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.module.read.recycler.ComicReadRecyclerActivity
import com.luuu.seven.util.*
import com.luuu.seven.widgets.BottomSheetBehavior
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_COLLAPSED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_EXPANDED
import com.luuu.seven.widgets.BottomSheetBehavior.Companion.STATE_HIDDEN
import kotlinx.android.synthetic.main.activity_comic_intro.*
import kotlinx.android.synthetic.main.include_comic_intro.*
import kotlinx.android.synthetic.main.intro_middle_layout.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   : 漫画详情界面
 *     version:
 */
class ComicIntroActivity : BaseActivity() {

    private lateinit var viewModel: IntroViewModel
    private var mLayoutManager: GridLayoutManager = GridLayoutManager(this, 4)
    private var mAdapter: BaseQuickAdapter<ChapterDataBean, BaseViewHolder>? = null
    private lateinit var mComicIntroBean: ComicIntroBean
    private var comicId = 0
    private var mHistoryBrowsePosition = 0
    private var mHistoryChapterPosition = 0
    private var isBack = false

    private var comicTitle = ""
    private val comicAuthors: StringBuilder by lazy { StringBuilder() }
    private var comicCover = ""
    private val comicTags: StringBuilder by lazy { StringBuilder() }
    private var isFavorite = false
    private lateinit var mMenu: Menu
    private var mFilterChapterList: List<ChapterDataBean>? = null
    private var mChapterList: ArrayList<ChapterDataBean>? = null
    private var isMoreThanEight = false

    private var readList = ArrayList<ReadHistoryBean>()

    companion object {
        const val COMIC_ID = "comicId"
    }

    override fun initViews() {

        val bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.chapter_sheet))

        tv_intro_title.click {
            bottomSheetBehavior.state = STATE_EXPANDED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val a11yState = if (newState == STATE_EXPANDED) {
                    View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
                } else {
                    View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
                }
//                comic_recyclerview.importantForAccessibility = a11yState
            }
        })
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.skipCollapsed = true
        if (bottomSheetBehavior.state == STATE_COLLAPSED) {
            bottomSheetBehavior.state = STATE_HIDDEN
        }

        viewModel = obtainViewModel<IntroViewModel>().apply {
            getReadHistory(comicId)
//            isFavorite(comicId)
//            getComicRelated(comicId)

            comicIntroData.observe(this@ComicIntroActivity, Observer { data ->
                data?.let {
                    updateComicData(it)
                }
            })

            updateFavorite.observe(this@ComicIntroActivity, Observer { bol ->
                bol?.let {
                    updateFavoriteMenu(it)
                }
            })

            readHistory.observe(this@ComicIntroActivity, Observer {
                readList.addAll(it)
                getComicIntro(comicId)
            })

            comicRelatedData.observe(this@ComicIntroActivity, Observer {

            })
        }
    }

    override fun getIntentExtras(extras: Bundle?) {
        comicId = extras?.getInt(COMIC_ID) ?: 0
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_intro


    private fun updateComicData(data: ComicIntroBean) {
        mChapterList = data.chapters[0].data
        filterChapter(data.chapters[0].data)
        mComicIntroBean = data

        if (mAdapter == null) {
            initAdapter()
        } else {
            mAdapter?.notifyDataSetChanged()
        }
        if (!isBack) {
            setData(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mMenu = menu!!
        menuInflater.inflate(R.menu.details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun updateFavoriteMenu(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        mMenu.findItem(R.id.menu_favorite)
            .setIcon(if (isFavorite) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favorite -> if (isFavorite) {
                viewModel.unFavoriteComic(comicId)
            } else {
                viewModel.favoriteComic(comicId, comicTitle, comicAuthors.toString(), comicCover, Date().time)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10002) {
            isBack = true
            viewModel.getComicIntro(comicId)
        }
    }

    private fun filterChapter(dataList: List<ChapterDataBean>) {
        if (dataList.size >= 9) {
            isMoreThanEight = true
            mFilterChapterList = dataList.take(8)
        }
    }

    fun initAdapter() {
        mAdapter = object : BaseQuickAdapter<ChapterDataBean, BaseViewHolder>(
            R.layout.item_chapter_layout,
            mFilterChapterList ?: mChapterList
        ) {

            override fun convert(holder: BaseViewHolder, item: ChapterDataBean?) {
                if (isMoreThanEight && holder.adapterPosition == (mFilterChapterList?.size ?: 1) - 1) {
                    holder.setText(R.id.tv_num, "...")
                } else {
                    holder.setText(R.id.tv_num, item?.chapterTitle)
                }

                if (mComicIntroBean.mReadHistoryBean != null && item?.chapterId == mComicIntroBean.mReadHistoryBean!!.chapterId) {
                    holder.itemView.setBackgroundResource(R.drawable.chapter_read_backgroud)
                    (holder.itemView as AppCompatTextView).setTextColor(0xFFFFFFFF.toInt())
                    mHistoryChapterPosition = holder.adapterPosition
                    mHistoryBrowsePosition =
                        if (mComicIntroBean.mReadHistoryBean!!.browsePosition < 1)
                            1
                        else
                            mComicIntroBean.mReadHistoryBean!!.browsePosition
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.chapter_backgroud)
                    (holder.itemView as AppCompatTextView).setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.content
                        )
                    )
                }
            }

        }

        chapter_grid.layoutManager = mLayoutManager
        chapter_grid.adapter = mAdapter

        mAdapter?.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.run {
                putInt("comicId", comicId)
                putParcelableArrayList("comicChapter", mChapterList)
                putInt("comicPosition", position)
                putString("comicTagName", mChapterList?.get(position)?.chapterTitle)
                putString("comicCover", mComicIntroBean.cover)
                putString("comicTitle", mComicIntroBean.title)
                putInt(
                    "historyPosition",
                    if (position + 1 == mHistoryChapterPosition) mHistoryBrowsePosition else 0
                )
            }
            startNewActivityForResult(ComicReadRecyclerActivity::class.java, 10002, mBundle)
        }
    }

    private fun setData(data: ComicIntroBean) {

        collap.title = data.title
        tv_intro_title.text = data.title

        data.authors.forEach {
            comicAuthors.append(it.tagName).append(" ")
        }

        data.types.forEach {
            comicTags.append(it.tagName).append(" ")
        }

        tv_intro_authors.text =
            string(R.string.intro_comic_authors, comicAuthors.toString())
        tv_intro_tags.text =
            string(R.string.intro_comic_types, comicTags.toString())

        comic_last_update.text =
            string(R.string.intro_comic_last_update, (data.lastUpdatetime * 1000).toDateString())
        comic_introduction.text = data.description
        iv_cha_img.loadWithHead(data.cover)
//        iv_cover.loadImg(data.cover)

        comicTitle = data.title
        comicCover = data.cover
    }

}
