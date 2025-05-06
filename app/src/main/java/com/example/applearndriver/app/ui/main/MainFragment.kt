package com.example.applearndriver.app.ui.main

import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.CategoryType
import com.example.applearndriver.data.model.MainCategoryModel
import com.example.applearndriver.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override val viewModel by activityViewModels<BaseViewModel>()

    private val categories = listOf(
        MainCategoryModel(R.drawable.ic_exam, EXAM_CATEGORY, CategoryType.EXAM),
        MainCategoryModel(R.drawable.ic_study, STUDY_CATEGORY, CategoryType.STUDY),
        MainCategoryModel(R.drawable.ic_signal, SIGNAL_CATEGORY, CategoryType.SIGNAL),
        MainCategoryModel(
            R.drawable.ic_tips_high_score,
            TIPS_HIGH_SCORE_CATEGORY,
            CategoryType.TIPS_HIGH_SCORE
        ),
        MainCategoryModel(
            R.drawable.ic_wrong_answer,
            WRONG_ANSWER_CATEGORY,
            CategoryType.WRONG_ANSWER
        ),
        MainCategoryModel(
            R.drawable.ic_driving_business,
            EXAM_GUIDE_CATEGORY,
            CategoryType.EXAM_GUIDE
        ),
        MainCategoryModel(
            R.drawable.ic_change_license_type_main_screen,
            CHANGE_LICENSE_TYPE_CATEGORY,
            CategoryType.CHANGE_LICENSE_TYPE
        ),
        MainCategoryModel(
            R.drawable.ic_settings,
            SETTINGS_CATEGORY,
            CategoryType.SETTINGS
        )
    )

    private val mainAdapter by lazy { MainCategoryListAdapter() }

    override fun initData() {
        // Not support
    }

    override fun handleEvent() {
        mainAdapter.registerOnClickItemEvent {
            when (it.type) {
                CategoryType.EXAM ->
                    findNavController().navigate(
                        R.id.action_nav_main_to_nav_exam,
                        bundleOf(
                            AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE to viewModel.currentLicenseType.type
                        )
                    )

                CategoryType.STUDY -> {
                    viewModel.setVisibleResetButton(true)
                    findNavController().navigate(R.id.action_nav_main_to_nav_study)
                }

                CategoryType.SIGNAL -> findNavController().navigate(R.id.action_nav_main_to_nav_traffic_sign)
                CategoryType.TIPS_HIGH_SCORE -> findNavController().navigate(R.id.action_nav_main_to_nav_tips_high_score)
                CategoryType.WRONG_ANSWER -> findNavController().navigate(R.id.action_nav_main_to_nav_wrong_answer)
                CategoryType.SETTINGS -> findNavController().navigate(R.id.action_nav_main_to_nav_settings)
                CategoryType.CHANGE_LICENSE_TYPE -> findNavController().navigate(R.id.action_nav_main_to_nav_change_license_type)
                CategoryType.EXAM_GUIDE -> findNavController().navigate(R.id.action_nav_main_to_nav_instruction)
            }
        }
    }

    override fun bindData() {
        viewBinding.recyclerViewCategory.adapter = mainAdapter
        mainAdapter.submitList(categories)
    }

    companion object {
        private const val EXAM_CATEGORY = "Thi sát hạch"
        private const val STUDY_CATEGORY = "Học lý thuyết"
        private const val SIGNAL_CATEGORY = "Biển báo đường bộ"
        private const val TIPS_HIGH_SCORE_CATEGORY = "Mẹo điểm cao"
        private const val WRONG_ANSWER_CATEGORY = "Các câu làm sai"
        private const val SETTINGS_CATEGORY = "Cài đặt"
        private const val EXAM_GUIDE_CATEGORY = "Bài thi thực hành"
        private const val CHANGE_LICENSE_TYPE_CATEGORY = "Đổi hạng bằng"
    }
}
