package com.example.applearndriver.app.ui.exam.detail

import android.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.applearndriver.app.ui.appadapter.QuestionDetailAdapter
import com.example.applearndriver.app.ui.dialog.BottomSheetQuestionDialog
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.constant.AppConstant.EMPTY_SIZE
import com.example.applearndriver.constant.AppConstant.FIRST_INDEX
import com.example.applearndriver.data.model.ExamState
import com.example.applearndriver.data.model.LicenseType
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.databinding.FragmentDetailExamLayoutBinding
import com.example.applearndriver.utils.interfaces.IBottomSheetListener
import com.example.applearndriver.utils.interfaces.IResponseListener
import com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.ExamViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailExamFragment :
    BaseFragment<FragmentDetailExamLayoutBinding>(FragmentDetailExamLayoutBinding::inflate) {

    private var listQuestionSize = EMPTY_SIZE

    private val bottomSheetDialog by lazy {
        BottomSheetQuestionDialog()
    }

    private val questionAdapter by lazy {
        QuestionDetailAdapter(isExamScreen = true)
    }

    private var initBottomSheetDisplayText = ""

    private val currentLicenseType
        get() = arguments?.getString(AppConstant.KEY_BUNDLE_CURRENT_LICENSE_TYPE) ?: LicenseType.A1.type

    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val isExamNotFinished =
                    viewModel.getCurrentExam()?.examState == ExamState.UNDEFINED.value

                if (isExamNotFinished) {
                    AlertDialog.Builder(context)
                        .setTitle(DIALOG_TITLE)
                        .setMessage(DIALOG_MESSAGE)
                        .setPositiveButton(YES_BUTTON) { _, _ ->
                            viewModel.saveCurrentExamState()
                            findNavController().navigateUp()
                        }
                        .setNegativeButton(NO_BUTTON) { _, _ -> }
                        .create()
                        .show()
                } else {
                    findNavController().navigateUp()
                }
            }
        }
    }

    override val viewModel by activityViewModels<ExamViewModel>()

    override fun initData() {
        viewBinding.viewPagerQuestions.adapter = questionAdapter

        val isExamNotFinished = viewModel.getCurrentExamTimestampLeft() != NO_TIME_LEFT

        if (isExamNotFinished) {
            questionAdapter.disableShowExplanation()
            questionAdapter.disableShowCorrectAnswer()
            viewModel.startCountDownEvent {
                findNavController().navigateUp()
            }
        } else {
            viewBinding.textCurrentTime.isVisible = false
            questionAdapter.disableClickEvent()
            (viewBinding.textCurrentQuestions.layoutParams as?
                    ConstraintLayout.LayoutParams)?.horizontalBias = CENTER_TEXT_QUESTION
            viewBinding.root.requestLayout()
        }
    }

    override fun handleEvent() {
        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateStateQuestion(questionPos, questionOptions)
            viewModel.navigateToNextQuestion(viewBinding.viewPagerQuestions.currentItem)
        }

        viewBinding.buttonNextQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                viewBinding.viewPagerQuestions.currentItem++
            }
        }

        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val textCurrentQuestionDisplay = "Câu ${viewBinding.viewPagerQuestions.currentItem + 1}/${listQuestionSize}"
                    initBottomSheetDisplayText = textCurrentQuestionDisplay
                    viewBinding.textCurrentQuestions.text = textCurrentQuestionDisplay
                    bottomSheetDialog.updateCurrentQuestionText(textCurrentQuestionDisplay)
                }
            }
        )

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem > FIRST_INDEX) {
                viewBinding.viewPagerQuestions.currentItem--
            }
        }

        viewBinding.bottomBar.setOnClickListener {
            context?.let { notNullContext ->
                bottomSheetDialog.initDialog(
                    initBottomSheetDisplayText,
                    notNullContext,
                    viewModel.getCurrentExam()?.listQuestionOptions,
                    viewBinding.viewPagerQuestions.currentItem,
                )

                bottomSheetDialog.setDialogEvent(
                    object : IBottomSheetListener {
                        override fun onNextQuestion(listener: IResponseListener<Int>) {
                            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                                viewBinding.viewPagerQuestions.currentItem++
                                listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                            } else {
                                listener.onError(null)
                            }
                        }

                        override fun onPreviousQuestion(listener: IResponseListener<Int>) {
                            if (viewBinding.viewPagerQuestions.currentItem > FIRST_INDEX) {
                                viewBinding.viewPagerQuestions.currentItem--
                                listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                            } else {
                                listener.onError(null)
                            }
                        }

                        override fun onClickMoveToPosition(index: Int, data: QuestionOptions) {
                            viewBinding.viewPagerQuestions.currentItem = index
                        }
                    })

                bottomSheetDialog.examDialogMode(
                    isExam = true,
                    isRunning = viewModel.getCurrentExamTimestampLeft() != NO_TIME_LEFT
                )

                bottomSheetDialog.showDialog()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    override fun bindData() {
        viewModel.currentTimeCountDown.observe(viewLifecycleOwner) {
            viewBinding.textCurrentTime.text = it
        }

        viewModel.currentExamQuestionPosition.observe(viewLifecycleOwner) {
            if (it != AppConstant.NONE_POSITION) {
                lifecycleScope.launch {
                    delay(CHANGE_TO_NEXT_QUESTION_DELAY_TIME)
                    viewBinding.viewPagerQuestions.currentItem = it
                }
            }
        }

        viewModel.listExam.observe(viewLifecycleOwner) {
            val currentExamPosition =
                viewModel.currentExamPosition.value ?: AppConstant.NONE_POSITION
            if (currentExamPosition != AppConstant.NONE_POSITION) {
                val currentExam = it[currentExamPosition]
                listQuestionSize = currentExam.listQuestions.size
                questionAdapter.submitList(currentExam.listQuestions)
                questionAdapter.updateQuestionStateList(currentExam.listQuestionOptions)
            }
        }
    }

    override fun onStop() {
        viewModel.saveTemporarilyExamStateWhenStop()
        super.onStop()
    }

    override fun onDestroyView() {
        viewModel.setVisibleFinishExamButton(false)
        viewModel.saveCurrentExamState()
        super.onDestroyView()
    }

    companion object {
        private const val CHANGE_TO_NEXT_QUESTION_DELAY_TIME = 700L
        private const val NO_TIME_LEFT = 0L
        private const val CENTER_TEXT_QUESTION = 0.5F
        private const val DIALOG_TITLE = "Thông báo"
        private const val DIALOG_MESSAGE = "Bạn có muốn thoát bài kiểm tra này không?"
        private const val YES_BUTTON = "Có"
        private const val NO_BUTTON = "Không"
    }
}
