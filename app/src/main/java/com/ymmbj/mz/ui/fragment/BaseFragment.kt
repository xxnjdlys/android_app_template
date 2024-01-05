package com.ymmbj.mz.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentBaseBinding

abstract class BaseFragment<T : ViewBinding>(@LayoutRes val layoutId: Int) : Fragment() {

    open lateinit var mViewBinding: T
    private val mLoadingMode: LoadingMode by lazy { LoadingMode() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val baseFragmentBinding: FragmentBaseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false)
        baseFragmentBinding.loading = mLoadingMode
        mViewBinding = DataBindingUtil.inflate(inflater, layoutId, baseFragmentBinding.contentLayout, true)
        initView()
        return baseFragmentBinding.root
    }

    abstract fun initView()

    fun initStatusBar(color: Int) {
        activity?.let {
            ImmersionBar.with(this).apply {
                fitsSystemWindows(true)
                statusBarColor(color)
                statusBarDarkFont(true, 0.2f)
                init()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    fun showLoading(show: Boolean, msg: String = "") {
        mLoadingMode.showLoading.set(show)
        if (!TextUtils.isEmpty(msg)) mLoadingMode.loadingTxt.set(msg)
    }

    class LoadingMode {
        val showLoading: ObservableField<Boolean> by lazy { ObservableField<Boolean>(false) }
        val loadingTxt: ObservableField<String> by lazy { ObservableField<String>("") }
    }

}