package com.example.applearndriver.app.ui.settings

import androidx.fragment.app.activityViewModels
import com.example.applearndriver.app.ui.settings.SettingsViewModel
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override val viewModel by activityViewModels<SettingsViewModel>()

    override fun initData() {
        viewBinding.switchDarkMode.isChecked = viewModel.isDarkModeOn.value ?: false
    }

    override fun handleEvent() {
        viewBinding.switchDarkMode.setOnCheckedChangeListener { _, _ ->
            if (viewBinding.switchDarkMode.isChecked.not()) {
                viewModel.turnOffDarkMode()
            } else {
                viewModel.turnOnDarkMode()
            }
        }
    }

    override fun bindData() {
        //Not-op
    }
}
