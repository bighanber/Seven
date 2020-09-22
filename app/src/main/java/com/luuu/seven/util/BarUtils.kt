package com.luuu.seven.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import com.luuu.seven.R
import java.util.regex.Pattern

object BarUtils {

    private const val DEFAULT_ALPHA = 0f
    private const val DEFAULT_COLOR = 0

    fun immersive(activity: Activity) {
        immersive(activity, DEFAULT_COLOR, DEFAULT_ALPHA)
    }

    fun immersive(activity: Activity, @ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        immersive(activity.window, color, alpha)
    }

    fun immersive(window: Window, @ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        when {
            Build.VERSION.SDK_INT >= 21 -> {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = mixtureColor(color, alpha)
                var systemUiVisibility = window.decorView.systemUiVisibility
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.decorView.systemUiVisibility = systemUiVisibility
            }
            Build.VERSION.SDK_INT >= 19 -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                setTranslucentView(
                    window.decorView as ViewGroup,
                    color,
                    alpha
                )
            }
            Build.VERSION.SDK_INT >= 16 -> {
                var systemUiVisibility = window.decorView.systemUiVisibility
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.decorView.systemUiVisibility = systemUiVisibility
            }
        }
    }

    fun darkMode(activity: Activity) {
        darkMode(
            activity.window,
            DEFAULT_COLOR,
            DEFAULT_ALPHA
        )
    }

    fun darkMode(activity: Activity, @ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        darkMode(activity.window, color, alpha)
    }

    fun darkMode(window: Window, @ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                darkModeForM(window, true)
                immersive(window, color, alpha)
            }
            isFlyme4() -> {
                setModeForFlyme4(window, true)
                immersive(window, color, alpha)
            }
            isMIUI6() -> {
                setModeForMIUI6(window, true)
                immersive(window, color, alpha)
            }
            Build.VERSION.SDK_INT >= 19 -> {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                setTranslucentView(
                    window.decorView as ViewGroup,
                    color,
                    alpha
                )
            }
        }
    }

    /**
     * android 6.0设置字体颜色
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun darkModeForM(window: Window, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = if (dark) {
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }

    /**
     * 设置MIUI6+的状态栏的darkMode,darkMode时候字体颜色及icon
     * http://dev.xiaomi.com/doc/p=4769/
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    private fun setModeForMIUI6(window: Window, dark: Boolean) {
        val clazz = window.javaClass
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.invoke(window, if (dark) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            Log.e("StatusBar", "darkIcon: failed")
        }

    }

    /**
     * 设置Flyme4+的状态栏的darkMode,darkMode时候字体颜色及icon
     * http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     *
     * @param window 目标window
     * @param dark   亮色 or 暗色
     */
    private fun setModeForFlyme4(window: Window, dark: Boolean) {
        try {
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (dark) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
        } catch (e: Exception) {
            Log.e("StatusBar", "darkIcon: failed")
        }

    }

    /**
     * 判断是否Flyme4以上
     */
    private fun isFlyme4(): Boolean {
        return (Build.FINGERPRINT.contains("Flyme_OS_4") || Build.VERSION.INCREMENTAL.contains("Flyme_OS_4")
                || Pattern.compile("Flyme OS [4|5]", Pattern.CASE_INSENSITIVE).matcher(Build.DISPLAY).find())
    }

    /**
     * 判断是否MIUI6以上
     */
    private fun isMIUI6(): Boolean {
        return try {
            val clz = Class.forName("android.os.SystemProperties")
            val mtd = clz.getMethod("get", String::class.java)
            var `val` = mtd.invoke(null, "ro.miui.ui.version.name") as String
            `val` = `val`.replace("[vV]".toRegex(), "")
            val version = Integer.parseInt(`val`)
            version >= 6
        } catch (e: Exception) {
            false
        }

    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度
     */
    fun setPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setPadding(
                view.paddingLeft,
                view.paddingTop +
                        getStatusBarHeight(context),
                view.paddingRight,
                view.paddingBottom
            )
        }
    }

    /**
     * 增加View的paddingTop,增加的值为状态栏高度 (智能判断，并设置高度)
     */
    fun setPaddingSmart(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= 16) {
            val lp = view.layoutParams
            if (lp != null && lp.height > 0) {
                lp.height += getStatusBarHeight(context) //增高
            }
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + getStatusBarHeight(context),
                view.paddingRight,
                view.paddingBottom
            )
        }
    }

    /**
     * 增加View的高度以及paddingTop,增加的值为状态栏高度.一般是在沉浸式全屏给ToolBar用的
     */
    fun setHeightAndPadding(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= 16) {
            val lp = view.layoutParams
            lp.height += getStatusBarHeight(context) //增高
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + getStatusBarHeight(context),
                view.paddingRight,
                view.paddingBottom
            )
        }
    }

    /**
     * 增加View上边距（MarginTop）一般是给高度为 WARP_CONTENT 的小控件用的
     */
    fun setMargin(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= 16) {
            val lp = view.layoutParams
            if (lp is MarginLayoutParams) {
                lp.topMargin += getStatusBarHeight(context) //增高
            }
            view.layoutParams = lp
        }
    }

    /**
     * 创建透明View
     *
     * @param viewGroup 目标视图
     * @param color     状态栏颜色值
     * @param alpha     状态栏透明度
     */
    private fun setTranslucentView(
        container: ViewGroup, @ColorInt color: Int, @FloatRange(
            from = 0.0,
            to = 1.0
        ) alpha: Float
    ) {
        if (Build.VERSION.SDK_INT >= 19) {
            val mixtureColor: Int = mixtureColor(color, alpha)
            var translucentView = container.findViewById<View>(R.id.custom)
            if (translucentView == null && mixtureColor != 0) {
                translucentView = View(container.context)
                translucentView.id = R.id.custom
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(container.context)
                )
                container.addView(translucentView, lp)
            }
            translucentView?.setBackgroundColor(mixtureColor)
        }

    }

    /**
     * 设置根布局参数
     *
     * @param activity         目标activity
     * @param fitSystemWindows 是否预留toolbar的高度
     */
    private fun setRootView(activity: Activity, fitSystemWindows: Boolean) {
        val parent = activity.findViewById<ViewGroup>(android.R.id.content)
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(fitSystemWindows)
                childView.clipToPadding = fitSystemWindows
            }
            i++
        }
    }

    fun mixtureColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) alpha: Float): Int {
        val a = if (color and -0x1000000 == 0) 0xff else color ushr 24
        return color and 0x00ffffff or ((a * alpha).toInt() shl 24)
    }

    /**
     * 获取状态栏高度
     *
     * @param context 目标Context
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 24
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        result = if (resId > 0) {
            context.resources.getDimensionPixelSize(resId)
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                result.toFloat(), Resources.getSystem().displayMetrics
            ).toInt()
        }
        return result
    }

    fun addStatusBarView(view: View, context: Context, @ColorInt color: Int) {
        val layoutParamsStatusBlock = view.layoutParams
        layoutParamsStatusBlock.height = getStatusBarHeight(context)
        view.setBackgroundColor(color)
        view.layoutParams = layoutParamsStatusBlock
    }
}