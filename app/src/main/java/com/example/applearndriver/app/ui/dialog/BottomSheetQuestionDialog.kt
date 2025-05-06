package com.example.applearndriver.app.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.applearndriver.app.ui.appadapter.BottomSheetQuestionListAdapter
import com.example.applearndriver.base.BaseDialogBinding
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.databinding.DialogBottomSheetQuestionsListBinding
import com.example.applearndriver.utils.CountDownInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.applearndriver.utils.interfaces.IBottomSheetListener
import com.example.applearndriver.utils.interfaces.IResponseListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BottomSheetQuestionDialog  : BaseDialogBinding<DialogBottomSheetQuestionsListBinding>(){
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var countDownExamCurrentJob: Job? = null
    private var _dialogBinding: DialogBottomSheetQuestionsListBinding? = null
    private val dialogBinding get() = _dialogBinding!!
    val bottomSheetAdapter by lazy { BottomSheetQuestionListAdapter() }

    private var isExamScreen = false

    fun updateCurrentQuestionText(currentText: String) {
        if(_dialogBinding != null) {
            dialogBinding.textCurrentQuestions.text = currentText
        }
    }

    fun initDialog(
        currentQuestionDisplayText: String,
        context: Context,
        listQuestionOptions: MutableList<QuestionOptions>?,
        currentSelectedPos: Int,
    ) {
        _dialogBinding =
            DialogBottomSheetQuestionsListBinding.inflate(LayoutInflater.from(context))

        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog?.apply {
            setContentView(dialogBinding.root)
            window?.attributes = WindowManager.LayoutParams().apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
            dialogBinding.recyclerViewQuestionIconList.adapter = bottomSheetAdapter
            dialogBinding.textCurrentQuestions.text = currentQuestionDisplayText
            bottomSheetAdapter.apply {
                submitList(listQuestionOptions)
                setSingleSelection(currentSelectedPos)
                dialogBinding.recyclerViewQuestionIconList.layoutManager?.scrollToPosition(
                    currentSelectedPos)
            }
        }
    }

    fun examDialogMode(isExam: Boolean, isRunning: Boolean) {
        isExamScreen = isExam
        bottomSheetAdapter.examScreenMode()
        if (isExam && isRunning) {
            setLayoutForTextInDetailExamScreen(dialogBinding)

            countDownExamCurrentJob = bottomSheetDialog?.lifecycleScope?.launch {
                while (CountDownInstance.CurrentTimeStamp != END_EXAM_TIME_STAMP) {
                    dialogBinding.textCurrentTime.text = CountDownInstance.CurrentTime
                    delay(INTERVAL_REQUEST_TIME_STRING)
                }
            }

            bottomSheetDialog?.setOnDismissListener {
                countDownExamCurrentJob?.cancel()
            }
        } else {
            dialogBinding.textCurrentTime.isVisible = false
            (dialogBinding.textCurrentQuestions.layoutParams as?
                    ConstraintLayout.LayoutParams)?.horizontalBias =
                SET_HORIZONTAL_BIAS_WHEN_EXAM_DONE
            dialogBinding.root.requestLayout()
        }
    }

    fun setDialogEvent(bottomSheetListener: IBottomSheetListener) {
        val positionListener = object : IResponseListener<Int> {
            override fun onSuccess(data: Int) {
                bottomSheetAdapter.setSingleSelection(data)
                dialogBinding.recyclerViewQuestionIconList.layoutManager?.scrollToPosition(data)
            }

            override fun onError(exception: Exception?) {
                //Not-op
            }
        }

        dialogBinding.buttonNextQuestion.setOnClickListener {
            bottomSheetListener.onNextQuestion(positionListener)
        }

        dialogBinding.buttonPreviousQuestion.setOnClickListener {
            bottomSheetListener.onPreviousQuestion(positionListener)
        }

        bottomSheetAdapter.registerOnClickItemPositionEvent { position, data ->
            bottomSheetAdapter.setSingleSelection(position)
            dialogBinding.recyclerViewQuestionIconList.layoutManager?.scrollToPosition(position)
            bottomSheetListener.onClickMoveToPosition(position, data)
        }
    }

    fun updateDataAdapter(listData: MutableList<QuestionOptions>) {
        bottomSheetAdapter.submitList(listData)
    }

    fun showDialog() = bottomSheetDialog?.show()

    private fun setLayoutForTextInDetailExamScreen(
        dialogBinding: DialogBottomSheetQuestionsListBinding,
    ) {
        (dialogBinding.textCurrentQuestions.layoutParams as ConstraintLayout.LayoutParams)
            .horizontalBias = SET_HORIZONTAL_BIAS_TEXT_CURRENT_QUESTION_IN_EXAM_SCREEN
        dialogBinding.textCurrentTime.isVisible = true
        dialogBinding.root.requestLayout()
    }

    companion object {
        private const val END_EXAM_TIME_STAMP = 0L
        private const val INTERVAL_REQUEST_TIME_STRING = 950L
        private const val SET_HORIZONTAL_BIAS_WHEN_EXAM_DONE = 0.5F
        private const val SET_HORIZONTAL_BIAS_TEXT_CURRENT_QUESTION_IN_EXAM_SCREEN = 0.35F
    }

    override fun getLayoutBinding(inflater: LayoutInflater): DialogBottomSheetQuestionsListBinding {
        TODO("Not yet implemented")
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}
