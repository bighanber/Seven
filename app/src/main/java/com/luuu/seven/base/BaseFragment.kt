package com.luuu.seven.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable

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
    private var fragmentRootView: View? = null
    private var isFirstResume = true
    private var isFirstVisible = true
    private var isFirstInvisible = true
    private var isPrepared = false
    private var isFragmentVisible: Boolean = false

    var mSubscription = CompositeDisposable()

    abstract fun onFragmentVisibleChange(isVisible: Boolean)
    abstract fun initViews()
    abstract fun getContentViewLayoutID(): Int
    abstract fun onFirstUserInvisible()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (fragmentRootView == null) {
            fragmentRootView = inflater.inflate(getContentViewLayoutID(), container, false)
        }
        return fragmentRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFragmentVisible) {
            onFragmentVisibleChange(true)
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFragmentVisible = isVisibleToUser
        if (fragmentRootView == null) {
            return
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(true)
        } else {
            onFragmentVisibleChange(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!mSubscription.isDisposed) {
            mSubscription.clear()
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