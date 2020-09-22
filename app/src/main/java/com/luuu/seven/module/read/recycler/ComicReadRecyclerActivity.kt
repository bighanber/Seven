package com.luuu.seven.module.read.recycler

import android.graphics.Point
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicReadAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.module.read.ReadViewModel
import com.luuu.seven.util.toast
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_comic_read_recycler.*
import kotlinx.android.synthetic.main.read_page_info.*

class ComicReadRecyclerActivity : BaseActivity() {

//    private val mLayoutManager: ViewPagerLayoutManager by lazy {
//        ViewPagerLayoutManager(
//            this,
//            LinearLayoutManager.HORIZONTAL,
//            false
//        )
//    }
    private val mViewModel: ReadViewModel by viewModels()
    private var mAdapter: ComicReadAdapter? = null
    private val mComicId: Int by lazy { intent.getIntExtra(COMIC_ID, 0) }
    private var intCurPage: Int = 0
    private var mTotalPage: Int = 0
    private val mChapters: List<ChapterDataBean> by lazy { intent.getParcelableArrayListExtra<ChapterDataBean>(COMIC_CHAPTER_LIST) }
    private var mCurChapterPosition: Int = 0
    private val mHistoryBrowsePosition: Int by lazy { intent.getIntExtra(COMIC_BROW_HISTORY_POS, 0) }
    private val mChapterTagName: String by lazy { intent.getStringExtra(COMIC_TAG_NAME) }
    private val mComicCover: String by lazy { intent.getStringExtra(COMIC_COVER) }
    private val mComicTitle: String by lazy { intent.getStringExtra(COMIC_TITLE) }
    private var isUse = true

    companion object {
        const val COMIC_ID = "comicId"
        const val COMIC_CHAPTER_LIST = "comicChapter"
        const val COMIC_TAG_NAME = "comicTagName"
        const val COMIC_CHAPTER_POS = "comicPosition"
        const val COMIC_BROW_HISTORY_POS = "historyPosition"
        const val COMIC_COVER = "comicCover"
        const val COMIC_TITLE = "comicTitle"
    }

    override fun initViews() {

        mCurChapterPosition = intent.getIntExtra(COMIC_CHAPTER_POS, 0)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mViewModel.getComicReadPage(mComicId, mChapters[mCurChapterPosition].chapterId)

        mViewModel.comicPageData.observe(this) {
            updateComicContent(it.pageUrl)
        }

        mViewModel.updateOrInsert.observe(this) {
            finish()
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                intCurPage = p1 + 1
//                updateProgressTest(intCurPage, mTotalPage)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
//                comic_list.smoothScrollToPosition(intCurPage)
            }

        })

        iv_back.setOnClickListener {
            saveHistory()
        }

//        mLayoutManager.setOnViewPagerListener(object : OnViewPagerListener {
//            override fun onPageRelease(isNext: Boolean, position: Int) {
//            }
//
//            override fun onPageSelected(position: Int, isTop: Boolean, isBottom: Boolean) {
//
//            }
//
//            override fun onLayoutComplete() {
//            }
//
//        })

        comic_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
                seek_bar.progress = position
                intCurPage = position
                toast("$position")
                tv_chapter_page.text = "${position + 1} | $mTotalPage"
                dealTopAndBottom(position == 0, position == mTotalPage - 1)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveHistory()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun saveHistory() {
        mViewModel.updateOrInsertReadData(
            ReadHistoryBean(
                mComicId,
                mChapters[mCurChapterPosition].chapterId,
                mChapterTagName,
                intCurPage,
                mComicCover,
                mComicTitle
            )
        )
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_read_recycler

    private fun updateComicContent(
        urls: MutableList<String>,
        bytes: MutableList<ByteArray>? = null,
        isFromDisk: Boolean = false
    ) {
        tv_chapter_title.text = mChapters[mCurChapterPosition].chapterTitle
        mTotalPage = if (isFromDisk) bytes!!.size else urls.size
        tv_chapter_page.text = "1 | $mTotalPage"
        seek_bar.max = mTotalPage - 1
        setAdapter(urls)

        if (isUse) {
            isUse = false
            intCurPage = mHistoryBrowsePosition
//            comic_list.scrollToPosition(intCurPage)
            comic_list.currentItem = intCurPage
        }
    }

    private fun setAdapter(urls: MutableList<String>) {
        if (mAdapter == null) {
            mAdapter = ComicReadAdapter(urls)
            comic_list.adapter = mAdapter
        } else {
            mAdapter?.setNewData(urls)
        }
        mAdapter?.getTapBack { x, y ->
            clickEvents(x, y)
        }
    }

    private fun clickEvents(x: Float, y: Float) {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        val limitX = point.x / 3.0f
        val limitY = point.y / 3.0f
        when {
            x * point.x < limitX -> {
                intCurPage -= 1
                if (intCurPage < 0) {
                    dealTopAndBottom(true, false)
                } else {
//                    comic_list.scrollToPosition(intCurPage)
                    comic_list.currentItem = intCurPage
                }
            }
            x * point.x > 2 * limitX -> {
                intCurPage += 1
                if (intCurPage >= mTotalPage) {
                    dealTopAndBottom(false, true)
                } else {
//                    comic_list.scrollToPosition(intCurPage)
                    comic_list.currentItem = intCurPage
                }
            }
            y * point.y < limitY -> {
                //上边
            }
            y * point.y > 2 * limitY -> {
                //下边
            }
            else -> switchControl()//剩余位置
        }
    }

    private fun dealTopAndBottom(isTop: Boolean, isBottom: Boolean) {
        if (isBottom) {
            intCurPage = 0
        } else if (isTop) {
            intCurPage = mTotalPage - 1
        }

        if (intCurPage == mTotalPage - 1) {
            if (mCurChapterPosition < mChapters.size - 1) {
                mCurChapterPosition += 1
                mViewModel.getComicReadPage(mComicId, mChapters[mCurChapterPosition].chapterId)
            } else {
                showToast(comic_list, "已经是第一话了")
            }
        } else if (intCurPage == 0) {
            if (mCurChapterPosition > 0) {
                mCurChapterPosition -= 1
                mViewModel.getComicReadPage(mComicId, mChapters[mCurChapterPosition].chapterId)
            } else {
                showToast(comic_list, "已经是最新的了")
            }
        }
    }

    private fun switchControl() {
        if (ll_bottom_bar.isShown) {
            hideBottomBar()
        } else {
            showBottomBar()
        }
    }

    private fun hideBottomBar() {
        val downAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f
        )
        downAction.duration = 300
        ll_bottom_bar.startAnimation(downAction)
        ll_bottom_bar.visibility = View.INVISIBLE
    }

    private fun showBottomBar() {
        val upAction = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.0f
        )
        upAction.duration = 300
        ll_bottom_bar.startAnimation(upAction)
        ll_bottom_bar.visibility = View.VISIBLE
    }

}
