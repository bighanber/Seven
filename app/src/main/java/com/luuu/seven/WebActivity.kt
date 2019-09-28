package com.luuu.seven

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.http.Api
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.get
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity() {

    private var mUrl: String?= null
    private var mWeb: WebView?= null

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        setToolbarTitle(" ")
        mWeb = findViewById(R.id.web)
        val mWebSettings = mWeb?.settings
        mWebSettings?.javaScriptEnabled = true
        mWebSettings?.useWideViewPort = true
        mWebSettings?.loadWithOverviewMode = true

        mUrl = intent.get("url")

        mWeb?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val uri = Uri.parse(url)
                if (uri.scheme == Api.SCHEME) {
                    if (uri.authority == Api.AUTHORITY) {
                        val id = uri.getQueryParameter("id")
                        val mBundle = Bundle()
                        mBundle.putInt("comicId", id.toInt())
                        startNewActivity(ComicIntroActivity::class.java, mBundle)
                    }
                } else {
                    view!!.loadUrl(url)
                }
                return true
            }
        }
        web.loadUrl(mUrl)

        iv_web_back.setOnClickListener {

            if (mWeb!!.canGoBack()) mWeb?.goBack() else onBackPressed()
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_web

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb!!.canGoBack()) {
            mWeb?.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWeb != null) {
            (mWeb?.parent as ViewGroup).removeView(mWeb)
            mWeb?.destroy()
            mWeb = null
        }
    }
}
