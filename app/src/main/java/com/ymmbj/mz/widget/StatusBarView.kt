package com.ymmbj.mz.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.ymmbj.mz.ktx.dpToPx

/**
 * 和系统statusbar 高度一致，全屏activity的占位控件
 */
class StatusBarView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mw = MeasureSpec.getSize(widthMeasureSpec)
        var mh = 20.dpToPx(context).toInt()
        context?.let {
            if (context is Activity) {
                mh = ImmersionBar.getStatusBarHeight(context)
            }
        }
        setMeasuredDimension(mw, mh)
    }

}