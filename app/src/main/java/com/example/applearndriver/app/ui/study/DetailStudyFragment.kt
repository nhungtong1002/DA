package com.example.applearndriver.app.ui.study

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.applearndriver.app.ui.appadapter.QuestionDetailAdapter
import com.example.applearndriver.app.ui.dialog.BottomSheetQuestionDialog
import com.example.applearndriver.base.BaseFragment
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.databinding.FragmentDetailStudyLayoutBinding
import com.example.applearndriver.utils.interfaces.IBottomSheetListener
import com.example.applearndriver.utils.interfaces.IResponseListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailStudyFragment :
    BaseFragment<FragmentDetailStudyLayoutBinding>(FragmentDetailStudyLayoutBinding::inflate) {

    private var listQuestionSize = AppConstant.EMPTY_SIZE
    override val viewModel by activityViewModels<StudyViewModel>()

    private val bottomSheetDialog by lazy {
        BottomSheetQuestionDialog()
    }

    private val questionAdapter by lazy { QuestionDetailAdapter() }

    private var isInitializeText: Boolean = false
    private var initBottomSheetDisplayText = ""

    private val bottomSheetDialogCallBack by lazy {
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
                if (viewBinding.viewPagerQuestions.currentItem > AppConstant.FIRST_INDEX) {
                    viewBinding.viewPagerQuestions.currentItem--
                    listener.onSuccess(viewBinding.viewPagerQuestions.currentItem)
                } else {
                    listener.onError(null)
                }
            }

            override fun onClickMoveToPosition(index: Int, data: QuestionOptions) {
                viewBinding.viewPagerQuestions.currentItem = index
            }
        }
    }

    override fun initData() {
        viewBinding.viewPagerQuestions.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val currentQuestionID = questionAdapter.currentList.getOrNull(position)?.id ?: 1
                    val textDisplay = "Câu ${currentQuestionID}/600"
                    initBottomSheetDisplayText = textDisplay
                    viewBinding.textCurrentQuestions.text = textDisplay
                    bottomSheetDialog.updateCurrentQuestionText(textDisplay)
                }
            }
        )

        viewBinding.viewPagerQuestions.adapter = questionAdapter
    }

    override fun handleEvent() {
        viewBinding.buttonNextQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem < listQuestionSize) {
                viewBinding.viewPagerQuestions.currentItem++
            }
        }

        viewBinding.buttonPreviousQuestion.setOnClickListener {
            if (viewBinding.viewPagerQuestions.currentItem > AppConstant.FIRST_INDEX) {
                viewBinding.viewPagerQuestions.currentItem--
            }
        }

        viewBinding.bottomBar.setOnClickListener {
            context?.let { notNullContext ->
                bottomSheetDialog.initDialog(
                    initBottomSheetDisplayText,
                    notNullContext,
                    viewModel.listStudyCategory.value?.get(
                        viewModel.currentStudyCategory.value
                            ?: AppConstant.NONE_POSITION
                    )
                        ?.listQuestionsState,
                    viewBinding.viewPagerQuestions.currentItem
                )
                bottomSheetDialog.setDialogEvent(bottomSheetDialogCallBack)
                bottomSheetDialog.showDialog()
            }
        }

        questionAdapter.setOnClickSelectedQuestionOption { questionID, questionPos, questionOptions ->
            viewModel.updateDataQuestionPos(questionID, questionPos, questionOptions)
            viewModel.saveProgressToDatabase()

            if (questionOptions.stateNumber == StateQuestionOption.INCORRECT.type) {
                lifecycleScope.launch {
                    viewModel.saveWrongAnswerObjectToDatabase(
                        WrongAnswer(
                            questionOptions.questionsID,
                            System.currentTimeMillis(),
                            viewModel.getSelectedStateWrongAnswerByID(questionOptions.questionsID)
                        )
                    )
                }
            }
        }
    }

    override fun bindData() {
        viewModel.listStudyCategory.observe(viewLifecycleOwner) {
            val currentPosition =
                viewModel.currentStudyCategory.value ?: AppConstant.NONE_POSITION
            listQuestionSize = it[currentPosition].totalNumberOfQuestions
            questionAdapter.submitList(it[currentPosition].listQuestions)
            questionAdapter.updateQuestionStateList(it[currentPosition].listQuestionsState)

            if (isInitializeText.not()) {
                viewBinding.textCurrentQuestions.text =
                    "Câu ${it[currentPosition].listQuestions.firstOrNull()?.id ?: 1}/600"
                isInitializeText = true
            }

            viewBinding.layoutVisibleWhenDataIsEmpty.root.isVisible =
                it[currentPosition].listQuestions.isEmpty()
            viewBinding.layoutVisibleWhenHaveData.isVisible =
                it[currentPosition].listQuestions.isEmpty().not()
        }
    }
}
