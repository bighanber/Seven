package com.luuu.seven.base

import android.os.Build
import androidx.appcompat.widget.Toolbar
import com.luuu.seven.R
import com.luuu.seven.util.BarUtils


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
abstract class BaseActivity : BaseAppCompatActivity() {

    private var mToolbar: Toolbar? = null

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mToolbar = findViewById(R.id.common_toolbar)
        mToolbar?.let {
            it.contentInsetStartWithNavigation = 0
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.elevation = 0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.elevation = 0f
            }

            it.setNavigationOnClickListener { onBackPressed() }
        }
        setStatusBar()
    }

    open fun setStatusBar() {
        BarUtils.immersive(this)
        mToolbar?.let {
            BarUtils.setPaddingSmart(this, it)
        }
    }

    fun setToolbarTitle(str: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = str
        }
    }

    fun setToolbarTitle(strId: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(strId)
        }
    }
}