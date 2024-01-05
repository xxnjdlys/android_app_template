package com.ymmbj.mz.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.ymmbj.mz.lifcycler.NetworkLifecycleObserver

open class BaseFragmentActivity<T : ViewBinding>(private val layoutId: Int, fitsSystemWindows: Boolean = false) : BaseActivity(fitsSystemWindows) {
    lateinit var mViewBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        setContentView(mViewBinding.root)
        this.lifecycle.addObserver(NetworkLifecycleObserver())
    }

}