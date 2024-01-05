package com.ymmbj.mz.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.ymmbj.mz.ktx.dpToPx
import com.ymmbj.mz.ktx.myApplication

object BindingUtils {

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(imageView: ImageView, url: String?) {
        ImageUtils.setImageUrl(imageView, url)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, uri: Uri) {
        ImageUtils.setImageUrl(view, uri)
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, dr: Drawable?) {
        ImageUtils.setImageUrl(view, dr)
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, b: Boolean) {
        if (b) view.visibility = View.VISIBLE
        else view.visibility = View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:selected")
    fun setSelected(view: View, b: Boolean) {
        view.isSelected = b
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(view: View, @ColorInt i: Int) {
        view.setBackgroundColor(i)
    }

    @JvmStatic
    @BindingAdapter("expandTouchArea")
    fun expandTouchArea(view: View, size: String) {
        view.postDelayed({
            val bounds = Rect()
            view.getHitRect(bounds)
            var left = 0
            var top = 0
            var right = 0
            var bottom = 0
            /*
            *  size 举例 `2` or `2 4` or `2 4 6 8`
             */
            val mSize = size.trim()
            val ss = mSize.split(" ")
            when (ss.size) {
                1 -> {
                    val sdp = (ss[0].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    left = sdp
                    top = sdp
                    right = sdp
                    bottom = sdp
                }
                2 -> {
                    val sdp = (ss[0].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    val sdp1 = (ss[1].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    left = sdp
                    top = sdp1
                    right = sdp
                    bottom = sdp1
                }
                4 -> {
                    left = (ss[0].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    top = (ss[1].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    right = (ss[2].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                    bottom = (ss[3].toIntOrNull() ?: 0).dpToPx(myApplication).toInt()
                }
                else -> {
                    return@postDelayed
                }
            }
            bounds.left -= left
            bounds.top -= top
            bounds.right += right
            bounds.bottom += bottom
            val mTouchDelegate = TouchDelegate(bounds, view);
            val p = view.parent
            if (p is ViewGroup) {
                p.touchDelegate = mTouchDelegate;
            }
        }, 100)
    }

    @JvmStatic
    @BindingAdapter("deltext")
    fun deltext(view: TextView, s: String) {
        val spanStr = SpannableString(s)
        spanStr.setSpan(StrikethroughSpan(), 0, s.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        view.text = spanStr
    }

}