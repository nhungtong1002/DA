package com.example.applearndriver.app.ui.changelicensetype

import androidx.fragment.app.viewModels
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.data.model.LicenseType
import com.example.applearndriver.data.model.LicenseTypeData
import com.example.applearndriver.databinding.FragmentChangeLicenseTypeScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLicenseTypeFragment :
    BaseFragment<FragmentChangeLicenseTypeScreenBinding>(FragmentChangeLicenseTypeScreenBinding::inflate) {

    override val viewModel by viewModels<ChangeLicenseTypeViewModel>()

    private val changeLicenseTypeAdapter by lazy { ChangeLicenseTypeAdapter() }

    override fun initData() {
        viewBinding.recyclerViewLicenseType.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = changeLicenseTypeAdapter
        }

        changeLicenseTypeAdapter.submitList(
            enumValues<LicenseType>().map { LicenseTypeData(it) }.toList())
    }

    override fun handleEvent() {
        changeLicenseTypeAdapter.registerOnClickItemEvent {
            viewModel.onChangingToNewLicenseType(it.licenseType)
        }
    }

    override fun bindData() {
        viewModel.currentLicenseType.observe(viewLifecycleOwner) {
            changeLicenseTypeAdapter.setCurrentLicenseType(it) { selectedPosition ->
                viewBinding.recyclerViewLicenseType.scrollToPosition(selectedPosition)
            }
        }
    }
}