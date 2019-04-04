package com.example.applearndriver.app.ui.exam.watchhistory

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.applearndriver.app.ui.main.MainActivity
import com.example.applearndriver.app.ui.exam.ExamViewModel
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.databinding.FragmentExamWatchHistoryLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExamWatchHistoryFragment
    : BaseFragment<FragmentExamWatchHistoryLayoutBinding>(FragmentExamWatchHistoryLayoutBinding::inflate) {

    override val viewModel by activityViewModels<ExamViewModel>()

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