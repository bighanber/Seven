package com.luuu.seven.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.webkit.WebView
import com.luuu.seven.ComicApplication
import java.util.*


class PreloadWebView private constructor() {

    companion object {
        val getInstance = SingletonHolder.holder
        private const val CACHED_WEBVIEW_MAX_NUM = 2
        private val mCachedWebViewStack: Stack<WebView> = Stack()
    }

    private object SingletonHolder {
        val holder = PreloadWebView()
    }

    fun preload() {
        Looper.myQueue().addIdleHandler {
            if (mCachedWebViewStack.size < CACHED_WEBVIEW_MAX_NUM) {
                mCachedWebViewStack.push(createWebView())
            }
            false
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView(): WebView {
        val webview = WebView(MutableContextWrapper(ComicApplication.mApp))
        webview.settings.javaScriptEnabled = true
        webview.loadDataWithBaseURL(
            "file:///android_asset/article/?item_id=0&token=0",
            getHtml(),
            "text/html",
            "utf-8",
            "file:///android_asset/article/?item_id=0&token=0"
        )
        return webview
    }


    private fun getHtml(): String? {
        val builder = StringBuilder()
        builder.append("<!DOCTYPE html>\n")
        builder.append("<html>\n")
        builder.append("<head>\n")
        builder.append("<meta charset=\"utf-8\">\n")
        builder.append("<meta name=\"viewport\" content=\"initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\">\n")
        builder.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"")
        builder.append("file:///android_asset/article/css/android.css")
        builder.append("\">\n</head>\n")
        builder.append("<body class=\"font_m\"><header></header><article></article><footer></footer>")
        builder.append("<script type=\"text/javascript\" src=\"")
        builder.append("file:///android_asset/article/js/lib.js")
        builder.append("\"></script>")
        builder.append("<script type=\"text/javascript\" src=\"")
        builder.append("file:///android_asset/article/js/android.js")
        builder.append("\" ></script>\n")
        builder.append("</body>\n")
        builder.append("</html>\n")
        return builder.toString()
    }

    /**
     * 从缓存池中获取合适的WebView
     *
     * @param context activity context
     * @return WebView
     */
    fun getWebView(context: Context?): WebView? {
        // 为空，直接返回新实例
        if (mCachedWebViewStack.isEmpty()) {
            val web = createWebView()
            val contextWrapper = web.context as MutableContextWrapper
            contextWrapper.baseContext = context
            return web
        }
        val webView = mCachedWebViewStack.pop()
        // webView不为空，则开始使用预创建的WebView,并且替换Context
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        return webView
    }
}