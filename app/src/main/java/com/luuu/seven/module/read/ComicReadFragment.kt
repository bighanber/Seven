package com.luuu.seven.module.read

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.os.BatteryManager
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicReadAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.util.gone
import com.luuu.seven.util.nav
import com.luuu.seven.util.show
import kotlinx.android.synthetic.main.fra_comic_read.*
import kotlinx.android.synthetic.main.fra_comic_read.iv_battery
import kotlinx.android.synthetic.main.fra_comic_read.iv_battery_charge
import kotlinx.android.synthetic.main.fra_comic_read.tv_battery
import kotlinx.android.synthetic.main.fra_comic_read.tv_chapter_page
import kotlinx.android.synthetic.main.fra_comic_read.tv_chapter_title

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

        mViewModel.comicPageData.observe(this) {
            updateComicContent(it.pageUrl)
        }

        mViewModel.updateOrInsert.observe(this) {
            nav().navigateUp()
        }

        seek_bar.addOnChangeListener { slider, value, fromUser ->
            intCurPage = value.toInt() - 1
            comic_list.currentItem = intCurPage
        }

        iv_back.setOnClickListener {
            saveHistory()
        }

        comic_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
                seek_bar.value = (position + 1).toFloat()
                intCurPage = position
                tv_chapter_page.text = "${position + 1} | $mTotalPage"
//                dealTopAndBottom(position == 0, position == mTotalPage - 1)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        view?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                saveHistory()
                return@setOnKeyListener true
            }
            false
        }
        requireActivity().registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(batteryReceiver)
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
            clickEvents(x, y)
        }
    }

    private fun clickEvents(x: Float, y: Float) {
        val point = Point()
        requireActivity().windowManager.defaultDisplay.getSize(point)
        val limitX = point.x / 3.0f
        val limitY = point.y / 3.0f
        when {
            x * point.x < limitX -> {
                intCurPage -= 1
                if (intCurPage < 0) {
                    dealTopAndBottom(true, false)
                } else {
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
            if (mCurChapterPosition < mChapters!!.size - 1) {
                mCurChapterPosition += 1
                mViewModel.getComicReadPage(mComicId, mChapters!![mCurChapterPosition].chapterId)
            } else {
                showToast("已经是第一话了")
            }
        } else if (intCurPage == 0) {
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

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (Intent.ACTION_BATTERY_CHANGED == p1?.action) {
                val level = p1.getIntExtra("level", 0)
                val scale = p1.getIntExtra("scale", 100)
                val status = p1.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)
                val power = level * 100 / scale
                val text = "$power%"
                tv_battery.text = text
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    iv_battery_charge.show()
                } else {
                    iv_battery_charge.gone()
                }
                val source = when {
                    power <= 10 -> R.drawable.ic_battery_10
                    power <= 30 -> R.drawable.ic_battery_30
                    power <= 50 -> R.drawable.ic_battery_50
                    power < 100 -> R.drawable.ic_battery_80
                    else -> R.drawable.ic_battery_full
                }
                iv_battery.setBackgroundResource(source)
            }

        }
    }
}
