package com.ymmbj.mz.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.gyf.immersionbar.ImmersionBar
import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.ActivityBaseBinding
import com.ymmbj.mz.ktx.myApplication

open class BaseActivity(private val fitsSystemWindows: Boolean = false) : AppCompatActivity() {

    lateinit var mRootBinding: ActivityBaseBinding
    private val actionBarMode = ActionBarMode()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransparentStatusBar(fitsSystemWindows)
        mRootBinding = ActivityBaseBinding.inflate(layoutInflater)
        super.setContentView(mRootBinding.root)
        mRootBinding.actionbar = actionBarMode
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    fun initTransparentStatusBar(fitsSystemWindows: Boolean = false) {
        ImmersionBar.with(this).apply {
            fitsSystemWindows(fitsSystemWindows)
            transparentStatusBar()
            statusBarDarkFont(true)
            init()
        }
    }

    fun setStatusBarColor(@ColorInt color: Int) {
        ImmersionBar.with(this).apply {
            statusBarColorInt(color)
            statusBarDarkFont(true, 0.2f)
            navigationBarColorInt(color)
            init()
        }
    }

    fun setNavigationBarColor(@ColorInt color: Int) {
        ImmersionBar.with(this).apply {
            navigationBarColorInt(color)
            init()
        }
    }

    fun hideActionBar() {
        mRootBinding.actionBar.visibility = View.GONE
    }

    override fun setContentView(view: View) {
        mRootBinding.content.addView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun setTitle(@StringRes title: Int) {
        setTitle(resources.getString(title))
    }

    override fun setTitle(title: CharSequence?) {
        if (title != null) setTitle(title.toString())
    }

    @Override
    fun setTitle(title: String) {
        actionBarMode.title.set(title)
        actionBarMode.hide.set(true)
    }

    fun setTitle(@StringRes title: Int, @ColorInt color: Int) {
        setTitle(resources.getString(title))
        actionBarMode.titleColor.set(color)
    }

    fun setRightText(@StringRes text: Int) {
        actionBarMode.rightText.set(resources.getString(text))
    }

    fun setRightText(text: String) {
        actionBarMode.rightText.set(text)
    }

    fun setLeftIcon(@DrawableRes icon: Int) {
        actionBarMode.leftIcon.set(icon)
        actionBarMode.hide.set(true)
    }

    fun setRightIcon(@DrawableRes icon: Int) {
        actionBarMode.rightIcon.set(icon)
    }

    fun setRightIcon2(@DrawableRes icon: Int) {
        actionBarMode.rightIcon2.set(icon)
    }

    fun setActionBarBackground(@ColorRes i: Int) {
        ContextCompat.getColor(myApplication, i).apply {
            setStatusBarColor(this)
            actionBarMode.background.set(this)
        }
    }

    open fun leftEvent() {
        if (leftEvent == null) {
            finish()
            return
        }
        leftEvent?.invoke()
    }

    open fun rightEvent() {
        rightEvent?.invoke()
    }

    open fun rightEvent2() {
        rightEvent2?.invoke()
    }

    fun showSearch() {
        actionBarMode.search.set(true)
    }

    var leftEvent: (() -> Unit?)? = null
    var rightEvent: (() -> Unit?)? = null
    var rightEvent2: (() -> Unit?)? = null

    fun setActionCallback(leftEvent: (() -> Unit?)?, rightEvent: (() -> Unit?)?, rightEvent2: (() -> Unit?)?) {
        this.leftEvent = leftEvent
        this.rightEvent = rightEvent
        this.rightEvent2 = rightEvent2
    }


    inner class ActionBarMode {

        val hide: ObservableField<Boolean> by lazy { ObservableField(false) }
        val search: ObservableField<Boolean> by lazy { ObservableField(false) }
        val leftIcon: ObservableField<Int> by lazy { ObservableField(R.mipmap.ic_actionbar_back) }
        val title: ObservableField<String> by lazy { ObservableField("") }
        val titleColor: ObservableField<Int> by lazy { ObservableField(ContextCompat.getColor(myApplication, R.color.color_333333)) }
        val rightIcon: ObservableField<Int> by lazy { ObservableField(0) }
        val rightIcon2: ObservableField<Int> by lazy { ObservableField(0) }
        val rightText: ObservableField<String> by lazy { ObservableField("") }
        val background: ObservableField<Int> by lazy { ObservableField(ContextCompat.getColor(myApplication, R.color.white)) }

        fun leftEvent() {
            this@BaseActivity.leftEvent()
        }

        fun rightEvent() {
            this@BaseActivity.rightEvent()
        }

        fun rightEvent2() {
            this@BaseActivity.rightEvent2()
        }
    }

}