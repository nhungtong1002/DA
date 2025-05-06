package com.example.applearndriver.app.ui.exam.result

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import com.example.applearndriver.R
import com.example.applearndriver.app.ui.MainActivity
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.data.model.ExamState
import com.example.applearndriver.data.model.LicenseType
import com.example.applearndriver.data.model.NewQuestionWithState
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.databinding.FragmentExamResultBinding
import com.example.applearndriver.utils.extensions.getCurrentThemeBackgroundColor
import com.example.applearndriver.utils.extensions.getSelectedColor
import com.example.applearndriver.utils.extensions.toDateTimeMMSS
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamFragment
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.isCurrentDarkMode
import dagger.hilt.android.migration.CustomInjection.inject


class ExamResultFragment
    : BaseFragment<FragmentExamResultBinding>(FragmentExamResultBinding::inflate) {

    override val viewModel by activityViewModels<ExamViewModel>()

    private val questionExamAdapter by lazy { ExamResultQuestionAdapter() }

    private val smoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    }

    private val currentLicenseType
        get() = arguments?.getString(AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE) ?: LicenseType.A1.type

    override fun initData() {
        (activity as? MainActivity)?.apply {
            addCallbackResetExamButton {
                val builder = AlertDialog.Builder(context)
                    .setTitle(ExamFragment.DIALOG_TITLE)
                    .setMessage("Bạn có muốn thi lại bài thi này không?")
                    .setPositiveButton(
                        "Thi ngay"
                    ) { _, _ ->
                        viewModel.resetStateCurrentExam(this@ExamResultFragment.currentLicenseType){
                            findNavController().apply {
                                popBackStack()
                                viewModel.setVisibleFinishExamButton(true)
                                navigate(R.id.action_nav_exam_to_nav_detail_exam)
                            }
                        }
                    }
                    .setNegativeButton("Thi sau") { _, _ ->
                        viewModel.resetStateCurrentExam(this@ExamResultFragment.currentLicenseType){
                            findNavController().popBackStack()
                        }
                    }
                    .setNeutralButton(ExamFragment.DIALOG_NEGATIVE_BUTTON_TEXT) { _, _ ->
                        //Not-op
                    }
                    .setCancelable(false)
                val dialog = builder.create()
                dialog.show()
            }
        }
        viewBinding.apply {
            val isDarkModeOn = inject<SharedPreferences>().value.isCurrentDarkMode()

            if (isDarkModeOn) {
                layoutExamResult.setBackgroundColor(getCurrentThemeBackgroundColor())
            } else {
                layoutExamResult.setBackgroundColor(getSelectedColor(R.color.background_color))
            }

            recyclerViewExamQuestion.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = questionExamAdapter
            }

        }
        viewModel.getCurrentExam()?.let { exam ->
            updateData(exam)
            viewBinding.buttonWatchExamHistory.setOnClickListener {
                findNavController().navigate(
                    R.id.action_nav_exam_result_to_nav_watch_history,
                    bundleOf(
                        AppConstant.KEY_BUNDLE_CURRENT_EXAM to exam,
                        AppConstant.KEY_BUNDLE_CURRENT_EXAM_POSITION to viewModel.currentExamPosition.value
                    )
                )
            }
        }
    }

    override fun handleEvent() {
        //Not implement
    }

    override fun bindData() {
        //Not implement
    }

    private fun updateData(exam: Exam) = with(viewBinding) {
        when (exam.examState) {
            ExamState.PASSED.value -> {
                textExamStateResult.apply {
                    text = "Đạt"
                    setBackgroundResource(R.drawable.bg_corner_20dp_green_pastel)
                }
                textExamResultDescription.text = "Bạn đã vượt qua bài kiểm tra này!!" 
            }

            ExamState.FAILED_BY_N0T_ENOUGH_SCORE.value -> {
                textExamStateResult.apply {
                    text = "Không đạt"
                    setBackgroundResource(R.drawable.bg_corner_20dp_red_pastel)
                }
                textExamResultDescription.text = "Số lượng câu đúng chưa đạt"
            }

            ExamState.FAILED_BY_MUST_NOT_WRONG_QUESTION.value  -> {
                textExamStateResult.apply {
                    text = "Không đạt"
                    setBackgroundResource(R.drawable.bg_corner_20dp_red_pastel)
                }

                textExamResultDescription.text = "Sai câu điểm liệt"
            }
        }

        textTimeDone.text = exam.timeExamDone.toDateTimeMMSS()

        textExamDone.text = "${exam.numbersOfCorrectAnswer}/${exam.listQuestionOptions.size}"

        textCorrectAnswer.text =
            exam.listQuestionOptions.count { selection -> selection.stateNumber == StateQuestionOption.CORRECT.type }
                .toString()
        textWrongAnswer.text =
            exam.listQuestionOptions.count { selection ->
                selection.stateNumber == StateQuestionOption.INCORRECT.type
                        && selection.position != AppConstant.NONE_POSITION
            }
                .toString()
        //Với các câu không trả lời được thì vị trí sẽ là -1
        textNotAnswered.text =
            exam.listQuestionOptions.count { selection -> selection.position == AppConstant.NONE_POSITION }
                .toString()

        questionExamAdapter.submitList(exam.listQuestions.map { NewQuestionWithState(it) })
        questionExamAdapter.updateQuestionStateList(exam.listQuestionOptions)

        questionExamAdapter.setUpdateCallBack {
            smoothScroller.targetPosition = it
            viewBinding.recyclerViewExamQuestion.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.removeCallbackResetExamButton()
        super.onDestroyView()
    }
}