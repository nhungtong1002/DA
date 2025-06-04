package com.example.applearndriver.app.ui.trafficsign

import androidx.fragment.app.viewModels
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.TrafficSigns
import com.example.applearndriver.databinding.FragmentDetailTrafficSignBinding
import com.example.applearndriver.utils.extensions.loadGlideImageFromUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailTrafficSignFragment
    : BaseFragment<FragmentDetailTrafficSignBinding>(FragmentDetailTrafficSignBinding::inflate){

    override val viewModel by viewModels<BaseViewModel>()

    override fun initData() {
        (arguments?.get(AppConstant.KEY_BUNDLE_TRAFFIC_SIGN) as? TrafficSigns)?.let {
            viewBinding.apply {
                imageTrafficSign.loadGlideImageFromUrl(
                    root.context,
                    it.imageUrl,
                )

                textIdTrafficSign.text = it.id
                textTitleTrafficSign.text = it.title
                textDescriptionTrafficSign.text = it.description
            }
        }
    }

    override fun handleEvent() {
        // Not op
    }

    override fun bindData() {

    }
}