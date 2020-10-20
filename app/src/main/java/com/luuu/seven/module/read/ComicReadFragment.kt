package com.luuu.seven.module.read

import android.graphics.Point
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicReadAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.util.*
import com.luuu.seven.util.observer.BatteryObserver
import kotlinx.android.synthetic.main.fra_comic_read.*

class ComicReadFragment : BaseFragment() {

    private val mViewModel: ReadViewModel by viewModels()
    private var mAdapter: ComicReadAdapter? = null
    private val mComicId: Int by lazy { arguments?.getInt(COMIC_ID) ?: 0 }
    private var intCurPage: Int = 0
    private var mTotalPage: Int = 0
    private val mChapters by lazy { arguments?.getParcelableArrayList<ChapterDataBean>(
        COMIC_CHAPTER_LIST
    ) }
    private var mCurChapterPosition: Int = 0
    private val mHistoryBrowsePosition: Int by lazy { arguments?.getInt(COMIC_BROW_HISTORY_POS) ?: 0 }
    private val mChapterTagName: String by lazy { arguments?.getString(COMIC_TAG_NAME) ?: "" }
    private val mComicCover: String by lazy { arguments?.getString(COMIC_COVER) ?: "" }
    private val mComicTitle: String by lazy { arguments?.getString(COMIC_TITLE) ?: "" }
    private var isUse = true
    private val mPoint by lazy {
        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        point
    }

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

        mCurChapterPosition = arguments?.getInt(COMIC_CHAPTER_POS) ?: 0

//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (mChapters.isNullOrEmpty()) return

        mViewModel.getComicReadPage(mComicId, mChapters?.get(mCurChapterPosition)?.chapterId ?: 0)

        seek_bar.addOnChangeListener { _, value, _ ->
            intCurPage = value.toInt() - 1
            comic_list.currentItem = intCurPage
        }

        iv_back.setOnClickListener {
            saveHistory()
        }

        comic_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                seek_bar.value = (position + 1).toFloat()
                intCurPage = position
                tv_chapter_page.text = "${position + 1} | $mTotalPage"
//                dealTopAndBottom(position == 0, position == mTotalPage - 1)
            }

        })

        BatteryObserver(requireContext(), this.lifecycle) {
            tv_battery.text = "${it.power}%"
            if (it.batteryCharging) {
                iv_battery_charge.show()
            } else {
                iv_battery_charge.gone()
            }
            val source = when {
                it.power <= 10 -> R.drawable.ic_battery_10
                it.power <= 30 -> R.drawable.ic_battery_30
                it.power <= 50 -> R.drawable.ic_battery_50
                it.power < 100 -> R.drawable.ic_battery_80
                else -> R.drawable.ic_battery_full
            }
            iv_battery.setBackgroundResource(source)
        }

        mViewModel.comicPageData.observe(viewLifecycleOwner) {
            updateComicContent(it.pageUrl)
        }

        mViewModel.updateOrInsert.observe(viewLifecycleOwner) {
            nav().navigateUp()
        }

        mViewModel.clickLeft.observeEvent(viewLifecycleOwner) {
            intCurPage -= 1
            if (intCurPage < 0) {
                intCurPage = 0
                dealTopAndBottom(true)
            } else {
                comic_list.currentItem = intCurPage
            }
        }

        mViewModel.clickRight.observeEvent(viewLifecycleOwner) {
            intCurPage += 1
            if (intCurPage >= mTotalPage) {
                intCurPage = mTotalPage
                dealTopAndBottom(false)
            } else {
                comic_list.currentItem = intCurPage
            }
        }

        mViewModel.clickMid.observeEvent(viewLifecycleOwner) {
            switchControl()
        }
    }

    override fun onResume() {
        super.onResume()
        view?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                saveHistory()
                return@setOnKeyListener true
            }
            false
        }
    }

    //    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            saveHistory()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    private fun saveHistory() {
        mViewModel.updateOrInsertReadData(
            ReadHistoryBean(
                mComicId,
                mChapters!![mCurChapterPosition].chapterId,
                mChapterTagName,
                intCurPage,
                mComicCover,
                mComicTitle
            )
        )
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_comic_read

    private fun updateComicContent(urls: MutableList<String>) {
        tv_chapter_title.text = mChapters!![mCurChapterPosition].chapterTitle
        mTotalPage = urls.size
        tv_chapter_page.text = "1 | $mTotalPage"
        seek_bar.valueTo = mTotalPage.toFloat()
        setAdapter(urls)

        if (isUse && mHistoryBrowsePosition > 0) {
            isUse = false
            intCurPage = mHistoryBrowsePosition
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
            mViewModel.checkClickPoint(x, y, mPoint)
        }
    }

    private fun dealTopAndBottom(isPrev: Boolean) {
        if (isPrev) {
            if (mCurChapterPosition < mChapters!!.size - 1) {
                mCurChapterPosition += 1
                mViewModel.getComicReadPage(mComicId, mChapters!![mCurChapterPosition].chapterId)
            } else {
                showToast("已经是第一话了")
            }
        } else {
            if (mCurChapterPosition > 0) {
                mCurChapterPosition -= 1
                mViewModel.getComicReadPage(mComicId, mChapters!![mCurChapterPosition].chapterId)
            } else {
                showToast("已经是最新的了")
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
        ll_bottom_bar.gone()
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
