package com.luuu.seven.module.read

import android.os.Bundle
import com.luuu.seven.R
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.util.loadImgWithProgress
import kotlinx.android.synthetic.main.fra_read_layout.*

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

        pv_read_page.loadImgWithProgress(mImgUrl!!,progress)
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_read_layout

    override fun onFirstUserInvisible() {
    }
}