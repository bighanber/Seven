package com.luuu.seven.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.luuu.seven.R
import com.luuu.seven.util.BarUtils
import com.luuu.seven.util.nav
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
    var fragmentRootView: View? = null
    private var mainToolbar: Toolbar? = null

    open fun onFragmentVisibleChange(isVisible: Boolean) {

    }
    abstract fun initViews()
    abstract fun getContentViewLayoutID(): Int

    open fun onFirstUserInvisible() {

    }

    open fun toolBarTitle() = ""

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
        mainToolbar = view.findViewById(R.id.common_toolbar)
        mainToolbar?.let {
            BarUtils.setPaddingSmart(requireContext(), it)
            it.contentInsetStartWithNavigation = 0
            (requireActivity() as AppCompatActivity).apply {
                setSupportActionBar(it)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            it.setNavigationOnClickListener { nav().navigateUp() }
        }
        initViews()
    }

    fun setToolbarTitle(title: String) {
        mainToolbar?.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
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