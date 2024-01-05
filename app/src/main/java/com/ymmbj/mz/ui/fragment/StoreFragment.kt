package com.ymmbj.mz.ui.fragment

import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentStoreBinding

class StoreFragment : BaseFragment<FragmentStoreBinding>(R.layout.fragment_store) {

    override fun initView() {
        mViewBinding.textview.text = "StoreFragment"
    }

}