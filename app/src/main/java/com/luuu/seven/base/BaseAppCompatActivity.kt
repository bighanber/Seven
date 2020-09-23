package com.luuu.seven.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {

    var TAG_LOG: String = BaseAppCompatActivity::class.java.simpleName

    lateinit var mContext: Context
    var extra: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID())
        } else {
            throw IllegalArgumentException("返回一个正确的ContentView")
        }
        initViews()
    }

    abstract fun initViews()
    abstract fun getContentViewLayoutID(): Int

    fun showToast(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    fun startNewActivity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    fun startNewActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    fun startNewActivityForResult(clazz: Class<*>, requestCode: Int, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }
}