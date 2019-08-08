package com.luuu.seven.module.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.format.DateFormat
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luuu.seven.R
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ChapterDataBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.module.read.recycler.ComicReadRecyclerActivity
import com.luuu.seven.util.*
import kotlinx.android.synthetic.main.activity_comic_intro.*
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

    private fun obtainViewModel(): IntroViewModel = obtainViewModel(IntroViewModel::class.java)
    private lateinit var viewModel: IntroViewModel
    private var mLayoutManager: GridLayoutManager = GridLayoutManager(this, 4)
    private var mAdapter: BaseQuickAdapter<ChapterDataBean, BaseViewHolder>? = null
    private lateinit var mComicIntroBean: ComicIntroBean
    private var comicId = 0
    private var mHistoryBrowsePosition = 0
    private var mHistoryChapterPosition = 0
    private var isBack = false

    private var comicTitle = ""
    private var comicAuthors = ""
    private var comicCover = ""
    private var comicTags = ""
    private var comicUpdataTime: Long? = null
    private var isFavorite = false
    private lateinit var mMenu: Menu

    private lateinit var headerView: View
    private lateinit var mUpdate: TextView
    private lateinit var mIntro: TextView

    override fun initViews() {
        BarUtils.setTranslucentForCoordinatorLayout(this, 0)
        headerView = LayoutInflater.from(this).inflate(R.layout.intro_list_header_layout, comic_recyclerview.parent as ViewGroup, false)
        mUpdate = headerView.findViewById(R.id.tv_intro_update)
        mIntro = headerView.findViewById(R.id.tv_cha_intro)

        viewModel = obtainViewModel().apply {
            getComicIntro(comicId, false).addTo(mSubscription)
            isFavorite(comicId)
        }.apply {
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
        }
    }

    override fun getIntentExtras(extras: Bundle?) {
        comicId = extras?.getInt("comicId") ?: 0
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_intro


    private fun updateComicData(data: ComicIntroBean) {
        if (mAdapter == null) {
            initAdapter(data.chapters[0].data)
        } else {
            mAdapter?.setNewData(data.chapters[0].data)
        }
        mComicIntroBean = data
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
        mMenu.findItem(R.id.menu_favorite).setIcon(if (isFavorite) R.drawable.ic_favorite_white else R.drawable.ic_favorite_border_white)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_favorite -> if (isFavorite) {
                viewModel.unFavoriteComic(comicId)
            } else {
                viewModel.favoriteComic(comicId, comicTitle, comicAuthors, comicCover, Date().time)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10002) {
            isBack = true
            viewModel.getComicIntro(comicId, false)
        }
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

        comic_recyclerview.layoutManager = mLayoutManager
        mAdapter?.addHeaderView(headerView)
        comic_recyclerview.adapter = mAdapter

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
            startNewActivityForResult(ComicReadRecyclerActivity::class.java, 10002, mBundle)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: ComicIntroBean) {
        mIntro.text = data.description
        collap.title = data.title
        tv_intro_title.text = data.title
        for (i in data.authors.indices) {
            comicAuthors = comicAuthors + data.authors[i].tagName + "/"
        }
        for (i in data.types.indices) {
            comicTags = comicTags + data.types[i].tagName + "/"
        }
        comicAuthors = comicAuthors.substring(0, comicAuthors.length - 1)
        comicTags = comicTags.substring(0, comicTags.length - 1)
        tv_intro_authors.text = "作者: $comicAuthors"
        tv_intro_tags.text = "类型: $comicTags"
        mUpdate.text = "最后更新: ${DateFormat.format("yyyy-MM-dd", data.lastUpdatetime * 1000)}"
        iv_cha_img.loadImgWithTransform(data.cover)
        iv_cover.loadImg(data.cover)

        comicTitle = data.title
        comicCover = data.cover
    }

}
