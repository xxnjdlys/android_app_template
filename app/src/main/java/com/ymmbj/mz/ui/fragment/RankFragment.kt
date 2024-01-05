package com.ymmbj.mz.ui.fragment

import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentRankBinding

class RankFragment : BaseFragment<FragmentRankBinding>(R.layout.fragment_rank) {

    override fun initView() {
        mViewBinding.textview.text = "RankFragment"
    }

}