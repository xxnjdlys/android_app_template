package com.ymmbj.mz.ui.fragment

import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentCenterBinding

class CenterFragment : BaseFragment<FragmentCenterBinding>(R.layout.fragment_center) {

    override fun initView() {
        mViewBinding.textview.text = "CenterFragment"
    }

}