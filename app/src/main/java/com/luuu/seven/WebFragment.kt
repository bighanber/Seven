package com.luuu.seven

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.http.Api
import kotlinx.android.synthetic.main.fra_web.*

class WebFragment : BaseFragment() {

    private var mUrl: String?= null
    private var mWeb: WebView?= null

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        mWeb = requireActivity().findViewById(R.id.web)
        val mWebSettings = mWeb?.settings
        mWebSettings?.javaScriptEnabled = true
        mWebSettings?.useWideViewPort = true
        mWebSettings?.loadWithOverviewMode = true

        mUrl = arguments?.getString("url")

        mWeb?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url)
                if (uri.scheme == Api.SCHEME) {
                    if (uri.authority == Api.AUTHORITY) {
                        val id = uri.getQueryParameter("id")
                        val bundle = Bundle().apply {
                            putInt("comicId", id?.toIntOrNull() ?: 0)
                        }
//                        startNewActivity(ComicIntroActivity::class.java, mBundle)
                        findNavController().navigate(R.id.action_web_fragment_to_intro_fragment, bundle)
                    }
                } else {
                    view!!.loadUrl(url)
                }
                return true
            }
        }
        web.loadUrl(mUrl)

        iv_web_back.setOnClickListener {

            if (mWeb!!.canGoBack()) mWeb?.goBack() else findNavController().navigateUp()
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_web

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb!!.canGoBack()) {
//            mWeb?.goBack()
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWeb != null) {
            (mWeb?.parent as ViewGroup).removeView(mWeb)
            mWeb?.destroy()
            mWeb = null
        }
    }
}
