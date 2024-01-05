package com.ymmbj.mz.ktx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.drakeet.multitype.ItemViewBinder

fun <T : ViewBinding> ViewBinding.createBindingVH() = BindingVH(this as T)

class BindingVH<T : ViewBinding>(val viewBinding: T) : RecyclerView.ViewHolder(viewBinding.root)

abstract class BaseItemViewBinder<T : ViewBinding>(private val layoutId: Int) : ItemViewBinder<AdapterData, BindingVH<T>>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BindingVH<T> {
        val viewBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false) as T
        return viewBinding.createBindingVH()
    }

}

abstract class SingleItemViewBinder<T, VB : ViewBinding>(private val layoutId: Int) : ItemViewBinder<T, BindingVH<VB>>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): BindingVH<VB> {
        val viewBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false) as VB
        return viewBinding.createBindingVH()
    }
}

