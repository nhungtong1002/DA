package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.watchhistory

import androidx.core.view.isVisible
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.Exam
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.FragmentExamWatchHistoryLayoutBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.mainscreen.MainActivity
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.constant.AppConstant
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ExamWatchHistoryFragment
    : BaseFragment<FragmentExamWatchHistoryLayoutBinding>(FragmentExamWatchHistoryLayoutBinding::inflate) {

    override val viewModel by sharedViewModel<ExamViewModel>()

    private val examWatchHistoryAdapter by lazy { ExamWatchHistoryAdapter() }

    override fun initData() {
        val currentExam = arguments?.get(AppConstant.KEY_BUNDLE_CURRENT_EXAM) as? Exam
        val currentExamPosition = arguments?.get(AppConstant.KEY_BUNDLE_CURRENT_EXAM_POSITION) as? Int
        if(currentExam == null) {
            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
        }

        currentExam?.let {
            (activity as? MainActivity)?.updateTitleToolbar("Đề ${currentExamPosition?.plus(1)} hạng ${it.examType}")

            viewBinding.recyclerViewExamHistory.apply {
                setHasFixedSize(true)
                adapter = examWatchHistoryAdapter
            }

            examWatchHistoryAdapter.submitList(it.listExamHistory)

            if(currentExam.listExamHistory.isEmpty()) {
                viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible = true
            }
        }
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        //Not implement
    }
}