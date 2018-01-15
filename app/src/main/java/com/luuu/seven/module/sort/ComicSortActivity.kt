package com.luuu.seven.module.sort

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicFragmentAdapter
import com.luuu.seven.adapter.ComicSortDialogAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.ComicSortBean
import com.luuu.seven.module.sort.sortlist.ComicSortListFragment
import kotlinx.android.synthetic.main.activity_comic_tab.*


class ComicSortActivity : BaseActivity(), ComicSortContract.View {

    private val mPresent by lazy { ComicSortPresenter(this) }
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mSortBeanList: List<ComicSortBean>? = null

    override fun initViews() {
        mSortBeanList = ArrayList()
        mPresent.getComicSort()
    }

    override fun onPause() {
        super.onPause()
        mPresent.unsubscribe()
        mSortBeanList = null
    }

    override fun getIntentExtras(extras: Bundle?) {
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_tab

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
    }

    override fun initViewPager(data: List<ComicSortBean>) {
        mSortBeanList = data
        val fragments = ArrayList<Fragment>()
        val title = ArrayList<String>()
        for (i in data.indices) {
            fragments.add(ComicSortListFragment.newInstance(data[i].tagId))
            title.add(data[i].title)
        }
        initViewPager(title, fragments)
    }

    private fun initViewPager(names: List<String>, fragments: List<Fragment>) {
        val adapter = ComicFragmentAdapter(supportFragmentManager,
                fragments, names)
        viewpager.adapter = adapter
        viewpager.setCurrentItem(0, false)
        viewpager.offscreenPageLimit = 1
        tabs.setupWithViewPager(viewpager)
        tabs.setScrollPosition(0, 0f, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showSortDialog()
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.sort_dialog_layout, null)
            val recyclerView = view.findViewById<View>(R.id.recycler_sort_dialog) as RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(this, 3)
            val adapter = ComicSortDialogAdapter(R.layout.item_chapter_layout, mSortBeanList!!)
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener { _, _, position ->
                mBottomSheetDialog!!.dismiss()
                val tab = tabs.getTabAt(position)
                tab?.select()
            }
            mBottomSheetDialog!!.setContentView(view)

            // 解决下滑隐藏dialog 后，再次调用show 方法显示时，不能弹出Dialog
            val view1 = mBottomSheetDialog!!.delegate.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(view1)
            bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        mBottomSheetDialog!!.dismiss()
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

            })
            mBottomSheetDialog!!.show()
        } else {
            mBottomSheetDialog!!.show()
        }
    }

}
