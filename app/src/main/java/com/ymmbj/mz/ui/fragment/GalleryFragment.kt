package com.ymmbj.mz.ui.fragment

import com.ymmbj.mz.R
import com.ymmbj.mz.databinding.FragmentGalleryBinding

class GalleryFragment : BaseFragment<FragmentGalleryBinding>(R.layout.fragment_gallery) {

    override fun initView() {
        mViewBinding.textview.text = "GalleryFragment"
    }

}