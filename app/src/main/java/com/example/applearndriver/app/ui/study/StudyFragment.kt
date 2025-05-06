package com.nguyennhatminh614.motobikedriverlicenseapp.screen.study

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.applearndriver.R
import com.example.applearndriver.app.ui.study.StudyAdapter
import com.example.applearndriver.app.ui.study.StudyViewModel
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.databinding.FragmentStudyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyFragment : BaseFragment<FragmentStudyBinding>(FragmentStudyBinding::inflate) {

    override val viewModel by activityViewModels<StudyViewModel>()

    private val baseViewModel by activityViewModels<BaseViewModel>()

    private val studyAdapter by lazy { StudyAdapter() }

    override fun initData() {
        baseViewModel.setVisibleResetButton(true)
        viewBinding.recyclerViewQuestionCategory.adapter = studyAdapter
    }

    override fun handleEvent() {
        studyAdapter.registerOnClickPositionEvent {
            viewModel.setCurrentStudyCategoryPosition(it)
            findNavController().navigate(R.id.action_nav_study_to_nav_detail_study)
        }
    }

    override fun bindData() {
        viewModel.listStudyCategory.observe(viewLifecycleOwner) {
            studyAdapter.submitList(it.map { it.copy() })
        }
    }

    override fun onDestroyView() {
        baseViewModel.setVisibleResetButton(false)
        super.onDestroyView()
    }
}
