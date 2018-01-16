package com.luuu.seven.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
abstract class BaseFragment : Fragment() {
    val TAG_LOG: String = BaseFragment::class.java.simpleName

    var mContext: Context? = null
    var fragmentRootView: View? = null
    var isFirstResume = true
    var isFirstVisible = true
    var isFirstInvisible = true
    var isPrepared = false

    abstract fun onFirstUserVisible()
    abstract fun onUserInvisible()
    abstract fun onUserVisible()
    abstract fun initViews()
    abstract fun getContentViewLayoutID(): Int
    abstract fun onFirstUserInvisible()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (fragmentRootView == null) {
            fragmentRootView = inflater!!.inflate(getContentViewLayoutID(), container, false)
        }
        val parent = fragmentRootView!!.parent as ViewGroup?
        parent?.removeView(fragmentRootView)
        return fragmentRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPrepare()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        if (userVisibleHint) {
            onUserVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint) {
            onUserInvisible()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false
                initPrepare()
            } else {
                onUserVisible()
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false
                onFirstUserInvisible()
            } else {
                onUserInvisible()
            }
        }
    }

    @Synchronized private fun initPrepare() {
        if (isPrepared) {
            onFirstUserVisible()
        } else {
            isPrepared = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isPrepared = false
    }

    override fun onDetach() {
        super.onDetach()
        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    protected fun startNewActivity(clazz: Class<*>) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
    }

    protected fun startNewActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun showToast(msg: String) {
        Snackbar.make(fragmentRootView!!, msg, Snackbar.LENGTH_SHORT).show()
    }
}