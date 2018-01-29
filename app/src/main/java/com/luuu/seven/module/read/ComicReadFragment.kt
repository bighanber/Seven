package com.luuu.seven.module.read

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.luuu.seven.MyApplication
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.http.Api
import kotlinx.android.synthetic.main.fra_read_layout.*
import java.lang.Exception

/**
 * Created by lls on 2017/8/7.
 *
 */
class ComicReadFragment : BaseFragment() {

    private var mImgUrl: String? = null
    private var mImgByte: ByteArray? = null
    private var isFromDisk: Boolean = false
//    private lateinit var mReadPage: PhotoView

    companion object {
        fun newInstance(imgUrl: String?, imgByte: ByteArray?, isFromDisk: Boolean): ComicReadFragment {
            val fragment = ComicReadFragment()
            val args = Bundle()
            args.putString("imgUrl", imgUrl)
            args.putByteArray("imgByte", imgByte)
            args.putBoolean("disk", isFromDisk)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstUserVisible() {
    }

    override fun onUserInvisible() {
    }

    override fun onUserVisible() {
    }

    override fun initViews() {
        arguments?.let {
            mImgUrl = it.getString("imgUrl")
            mImgByte = it.getByteArray("imgByte")
            isFromDisk = it.getBoolean("disk")
        }

        pv_read_page.setOnPhotoTapListener { _, x, y ->
            val act = activity
            if (act is ComicReadActivity) {
                (activity as ComicReadActivity).clickEvents(x, y)
            }
        }

        Glide.with(MyApplication.sAppContext)
                .load(GlideUrl(mImgUrl, LazyHeaders.Builder().addHeader("Referer", Api.DMZJ).build()))
                .listener(object : RequestListener<GlideUrl, GlideDrawable> {
                    override fun onResourceReady(resource: GlideDrawable?, model: GlideUrl?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        progress.visibility = View.INVISIBLE
                        return false
                    }

                    override fun onException(e: Exception?, model: GlideUrl?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                })
                .into(pv_read_page)
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_read_layout

    override fun onFirstUserInvisible() {
    }
}