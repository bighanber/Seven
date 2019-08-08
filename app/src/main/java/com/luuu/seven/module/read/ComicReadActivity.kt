package com.luuu.seven.module.read

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.os.BatteryManager
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import androidx.viewpager.widget.ViewPager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicReadPagerAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ChapterDataBean
import kotlinx.android.synthetic.main.activity_comic_read.*
import kotlinx.android.synthetic.main.read_page_info.*

/***
 * 阅读界面
 */
class ComicReadActivity : BaseActivity(), ComicReadContract.View {

    private var mPagerAdapter: ComicReadPagerAdapter? = null
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
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        gallery_pager.setLocked(true)
        gallery_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                seek_bar.progress = position - 1
                intCurPage = position
                if (intCurPage > mPagerAdapter!!.count - 2) {
                    intCurPage = mPagerAdapter!!.count - 2
                } else if (intCurPage < 1) {
                    intCurPage = 1
                }
                updataProgress(intCurPage, mTotalPage)

                if (position == 0) {
                    gallery_pager.setLocked(true)
                    seek_bar.isEnabled = false
                    if (mCurChapterPosition < mChapters!!.size - 1) {
                        mCurChapterPosition += 1
                        mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)
                    } else {
                        gallery_pager.currentItem = 1
                        gallery_pager.setLocked(false)
                        seek_bar.isEnabled = true
                        showToast(gallery_pager,"已经是第一话了")
                    }
                } else if (position == mPagerAdapter!!.count - 1) {
                    gallery_pager.setLocked(true)
                    seek_bar.isEnabled = false
                    if (mCurChapterPosition > 0) {
                        mCurChapterPosition -= 1
                        mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)
                    } else {
                        gallery_pager.currentItem = mPagerAdapter!!.count - 2
                        gallery_pager.setLocked(false)
                        seek_bar.isEnabled = true
                        showToast(gallery_pager,"已经是最新的了")
                    }
                }
            }

        })

        mPagerAdapter = ComicReadPagerAdapter(supportFragmentManager)
        gallery_pager.adapter = mPagerAdapter
        seek_bar.isEnabled = false
        seek_bar.keyProgressIncrement = 1
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            internal var progress = 0
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                progress = p1
                updataProgress(progress + 1, mTotalPage)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                gallery_pager.setCurrentItem(progress + 1, false)
            }

        })
        iv_back.setOnClickListener{
            mPresenter.updateReadHistory(mComicId, mChapters!![mCurChapterPosition].chapterId,
                    mChapterTagName!!, intCurPage, mComicCover!!, mComicTitle!!)
        }
        tv_chapter_title.text = mChapters!![mCurChapterPosition].chapterTitle
        gallery_pager.setCurrentItem(1, false)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.updateReadHistory(mComicId, mChapters!![mCurChapterPosition].chapterId,
                    mChapterTagName!!, intCurPage, mComicCover!!, mComicTitle!!)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getIntentExtras(extras: Bundle?) {
        extras?.let {
            mComicId = it.getInt("comicId")
            mChapters = it.getParcelableArrayList<Parcelable>("comicChapter") as ArrayList<ChapterDataBean>
            mChapterTagName = it.getString("comicTagName")
            mCurChapterPosition = it.getInt("comicPosition")
            mHistoryBrowsePosition = it.getInt("historyPosition")
            mComicCover = it.getString("comicCover")
            mComicTitle = it.getString("comicTitle")
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_read

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getComicData(mComicId, mChapters!![mCurChapterPosition].chapterId)
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryReceiver)
        mPresenter.unsubscribe()
    }

    override fun updateComicContent(urls: MutableList<String>, bytes: MutableList<ByteArray>?, isFromDisk: Boolean) {
        tv_chapter_title.text = mChapters!![mCurChapterPosition].chapterTitle
        val count = if (isFromDisk) bytes!!.size else urls.size
        mTotalPage = count
        updataProgress(1, mTotalPage)
        seek_bar.max = count - 1
        gallery_pager.setCurrentItem(1, false)
        mPagerAdapter!!.replaceAll(urls, bytes, isFromDisk)
        gallery_pager.setLocked(false)
        seek_bar.isEnabled = true

        if (isUse) {
            isUse = false
            gallery_pager.setCurrentItem(mHistoryBrowsePosition, false)
        }
    }

    override fun isSuccess(isOver: Boolean) {
        if (isOver) onBackPressed() else showToast(gallery_pager, "数据更新失败")
    }

    @SuppressLint("SetTextI18n")
    private fun updataProgress(curPage: Int, totalPage: Int) {
        tv_chapter_page.text = curPage.toString() + " | " + totalPage
    }

    fun clickEvents(x: Float, y: Float) {
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        val limitX = point.x / 3.0f
        val limitY = point.y / 3.0f
        when {
            x * point.x < limitX -> {
                intCurPage -= 1
                gallery_pager.currentItem = intCurPage//左边
            }
            x * point.x > 2 * limitX -> {
                intCurPage += 1
                gallery_pager.currentItem = intCurPage //右边
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

    private fun switchControl() {
        if (ll_bottom_bar.isShown) {
            hideBottomBar()
        } else {
            showBottomBar()
        }
    }

    private fun hideBottomBar() {
        val downAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f)
        downAction.duration = 300
        ll_bottom_bar.startAnimation(downAction)
        ll_bottom_bar.visibility = View.INVISIBLE
    }

    private fun showBottomBar() {
        val upAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f)
        upAction.duration = 300
        ll_bottom_bar.startAnimation(upAction)
        ll_bottom_bar.visibility = View.VISIBLE
    }

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (Intent.ACTION_BATTERY_CHANGED == intent.action) {
                val level = intent.getIntExtra("level", 0)
                val scale = intent.getIntExtra("scale", 100)
                val status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)
                val power = level * 100 / scale
                val text = power.toString() + "%"
                tv_battery.text = text
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    iv_battery_charge.visibility = View.VISIBLE
                } else {
                    iv_battery_charge.visibility = View.GONE
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
