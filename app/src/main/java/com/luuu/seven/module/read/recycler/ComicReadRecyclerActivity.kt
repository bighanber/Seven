package com.luuu.seven.module.read.recycler

import android.graphics.Point
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicReadAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.module.read.ComicReadContract
import com.luuu.seven.module.read.ComicReadPresenter
import com.luuu.seven.util.get
import com.luuu.seven.util.toast
import kotlinx.android.synthetic.main.activity_comic_read_recycler.*
import kotlinx.android.synthetic.main.read_page_info.*

class ComicReadRecyclerActivity : BaseActivity(), ComicReadContract.View {

    private val mLayoutManager: ViewPagerLayoutManager by lazy {
        ViewPagerLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }
    private var mAdapter: ComicReadAdapter? = null
    private var mComicId: Int = 0
    private var intCurPage: Int = 0
    private var mTotalPage: Int = 0
    private var mChapters: List<ChapterDataBean>? = null
    private var mCurChapterPosition: Int = 0
    private var mHistoryBrowsePosition: Int = 0
    private var mChapterTagName: String? = null
    private var mComicCover: String? = null
    private var mComicTitle: String? = null
    private var isUse = true
    private val mPresenter by lazy { ComicReadPresenter(this) }

    override fun initViews() {

        mComicId = intent.get("comicId") ?: 0
        mChapters = intent.get("comicChapter")
        mChapterTagName = intent.get("comicTagName")
        mCurChapterPosition = intent.get("comicPosition") ?: 0
        mHistoryBrowsePosition = intent.get("historyPosition") ?: 0
        mComicCover = intent.get("comicCover")
        mComicTitle = intent.get("comicTitle")

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                intCurPage = p1 + 1
//                updateProgressTest(intCurPage, mTotalPage)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                comic_list.smoothScrollToPosition(intCurPage)
            }

        })

        iv_back.setOnClickListener {
            mPresenter.updateReadHistory(
                mComicId, mChapters!![mCurChapterPosition].chapterId,
                mChapterTagName!!, intCurPage, mComicCover!!, mComicTitle!!
            )
        }

        mLayoutManager.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onPageRelease(isNext: Boolean, position: Int) {
            }

            override fun onPageSelected(position: Int, isTop: Boolean, isBottom: Boolean) {
                seek_bar.progress = position
                intCurPage = position
                toast("$position")
                tv_chapter_page.text = "${position + 1} | $mTotalPage"
                dealTopAndBottom(isTop, isBottom)
            }

            override fun onLayoutComplete() {
            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.updateReadHistory(
                mComicId, mChapters!![mCurChapterPosition].chapterId,
                mChapterTagName!!, intCurPage, mComicCover!!, mComicTitle!!
            )
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_read_recycler

    override fun updateComicContent(
        urls: MutableList<String>,
        bytes: MutableList<ByteArray>?,
        isFromDisk: Boolean
    ) {
        tv_chapter_title.text = mChapters!![mCurChapterPosition].chapterTitle
        mTotalPage = if (isFromDisk) bytes!!.size else urls.size
        tv_chapter_page.text = "1 | $mTotalPage"
        seek_bar.max = mTotalPage - 1
        setAdapter(urls)

        if (isUse) {
            isUse = false
            intCurPage = mHistoryBrowsePosition
            comic_list.scrollToPosition(intCurPage)
        }
    }

    private fun setAdapter(urls: MutableList<String>) {
        if (mAdapter == null) {
            mAdapter = ComicReadAdapter(urls)
            comic_list.layoutManager = mLayoutManager
            comic_list.adapter = mAdapter
        } else {
            mAdapter?.setNewData(urls)
        }
        mAdapter?.getTapBack { x, y ->
            clickEvents(x, y)
        }
    }

    override fun isSuccess(isOver: Boolean) {
        if (isOver) onBackPressed() else showToast(comic_list, "数据更新失败")
    }

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun onPause() {
        super.onPause()
        mPresenter.unsubscribe()
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
                    comic_list.scrollToPosition(intCurPage)
                }
            }
            x * point.x > 2 * limitX -> {
                intCurPage += 1
                if (intCurPage >= mTotalPage) {
                    dealTopAndBottom(false, true)
                } else {
                    comic_list.scrollToPosition(intCurPage)
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
            if (mCurChapterPosition < mChapters!!.size - 1) {
                mCurChapterPosition += 1
                mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)
            } else {
                showToast(comic_list, "已经是第一话了")
            }
        } else if (intCurPage == 0) {
            if (mCurChapterPosition > 0) {
                mCurChapterPosition -= 1
                mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)
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
