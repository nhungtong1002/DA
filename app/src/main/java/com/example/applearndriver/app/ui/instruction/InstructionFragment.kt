package com.example.applearndriver.app.ui.instruction

import android.content.Context
import android.content.SharedPreferences
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.webkit.WebViewAssetLoader
import com.example.applearndriver.app.ui.main.MainActivity
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.databinding.FragmentInstructionBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.instruction.LocalContentWebViewClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject
@AndroidEntryPoint
class InstructionFragment :
    BaseFragment<FragmentInstructionBinding>(FragmentInstructionBinding::inflate) {

    override val viewModel by viewModels<InstructionViewModel>()

    override fun initData() {
        //Not implement
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        viewModel.isMotorbikeLicenseType.observe(viewLifecycleOwner) { isMotorbike ->
            if (isMotorbike) {
                viewBinding.layoutMotorbikeInstruction.isVisible = true
                viewBinding.webViewInstruction.isVisible = false
            } else {
                viewBinding.layoutMotorbikeInstruction.isVisible = false
                viewBinding.webViewInstruction.isVisible = true
                context?.let { loadCarExamInstruction(it) }
            }
        }
    }

    private fun loadCarExamInstruction(context: Context){
        val assetPathHandler = WebViewAssetLoader.AssetsPathHandler(context)
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(ASSET_PATH_HANDLER_KEY, assetPathHandler)
            .build()

        viewBinding.webViewInstruction.settings.apply {
            allowContentAccess = true
            allowFileAccess = true
        }

        viewBinding.webViewInstruction.apply {
            webViewClient = LocalContentWebViewClient(
                assetLoader,
                loadStartCallback = {
                    (activity as? MainActivity)?.showLoadingDialog()
                },
                loadFinishCallback = {
                    (activity as? MainActivity)?.hideLoadingDialog()
                }
            )

            if (viewModel.isDarkModeOn) {
                loadUrl(INSTRUCTION_WEB_URL_DARK_MODE)
            } else {
                loadUrl(INSTRUCTION_WEB_URL_LIGHT_MODE)
            }
        }
    }

    companion object {
        const val INSTRUCTION_WEB_URL_DARK_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_dark_mode.html"
        const val INSTRUCTION_WEB_URL_LIGHT_MODE =
            "https://appassets.androidplatform.net/assets/thuchanh_light_mode.html"
        const val ASSET_PATH_HANDLER_KEY = "/assets/"
    }
}