package com.example.applearndriver.app.ui.exam.result

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.applearndriver.R
import com.example.applearndriver.app.ui.appadapter.QuestionOptionAdapter
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.NewQuestionWithState
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.databinding.ItemExamQuestionResultScreenBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.extensions.loadGlideImageFromUrl
import com.example.applearndriver.utils.extensions.processExplainQuestion
import com.example.applearndriver.utils.processQuestionOptionsList

class ExamResultQuestionAdapter :
    BaseRecyclerViewAdapter<NewQuestionWithState, ItemExamQuestionResultScreenBinding, ExamResultQuestionAdapter.ViewHolder>(
        NewQuestionWithState.getDiffCallBack()
    ) {

    private var updateCallback : ((Int) -> Unit)? = null

    private val listQuestionState = mutableListOf<QuestionOptions>()

    fun setUpdateCallBack(callback: (Int) -> Unit) {
        this.updateCallback = callback
    }

    fun updateQuestionStateList(listQuestionState: List<QuestionOptions>) {
        this.listQuestionState.clear()
        this.listQuestionState.addAll(listQuestionState)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemExamQuestionResultScreenBinding) :
        BaseViewHolder<NewQuestionWithState, ItemExamQuestionResultScreenBinding>(binding) {
        private fun NewQuestionWithState.visibleWithAnimation() {
            if(this.isVisible) {
                binding.buttonSpanContent.animate().rotation(0f).start()
                binding.viewExpandedInformation.isVisible = true
            } else {
                binding.buttonSpanContent.animate().rotation(180f).start()
                binding.viewExpandedInformation.isVisible = true
            }
        }
        override fun onBindData(data: NewQuestionWithState) {
            val questionOptionAdapter by lazy { QuestionOptionAdapter() }

            data.let { elem ->
                binding.apply {
                    textTitleQuestions.text = "Câu ${adapterPosition + 1}${if(data.newQuestion.isImmediateFailedTestWhenWrong) " [ĐIỂM LIỆT]" else ""}: ${elem.newQuestion.question}"
                    textQuestionExplain.text = elem.newQuestion.explain.processExplainQuestion()

                    if(elem.isVisible) {
                        binding.buttonSpanContent.rotation = 0f
                        viewExpandedInformation.isVisible = true
                    } else {
                        binding.buttonSpanContent.rotation = 180f
                        viewExpandedInformation.isVisible = false
                    }

                    if (elem.newQuestion.hasImageBanner) {
                        imageQuestions.isVisible = true
                        imageQuestions.loadGlideImageFromUrl(
                            root.context,
                            elem.newQuestion.image
                        )
                    } else {
                        imageQuestions.isVisible = false
                    }

                    recyclerViewQuestionOptions.adapter = questionOptionAdapter

                    val listQuestionOptions = processQuestionOptionsList(
                        questionsID = elem.newQuestion.id,
                        listString = elem.newQuestion.listOption
                    )

                    questionOptionAdapter.submitList(listQuestionOptions)
                    questionOptionAdapter.updateCorrectAnswerPosition(elem.newQuestion.correctAnswerPosition)

                    val selectedPosition = listQuestionState[adapterPosition].position

                    if(selectedPosition != AppConstant.NONE_POSITION){
                        listQuestionOptions[selectedPosition] =
                            listQuestionState[adapterPosition].copy()
                        questionOptionAdapter.updateStateListWithPosition(listQuestionOptions, selectedPosition)

                    } else {
                        questionOptionAdapter.submitList(listQuestionOptions)
                    }

                    val questionState = listQuestionState[adapterPosition]

                    if(questionState.position == AppConstant.NONE_POSITION) {
                        imageQuestionsState.setImageResource(R.drawable.ic_not_answered)
                        textQuestionState.text = "Không chọn"
                    } else {
                        if(questionState.stateNumber == StateQuestionOption.CORRECT.type) {
                            imageQuestionsState.setImageResource(R.drawable.ic_verified)
                            textQuestionState.text = "Đáp án đúng"
                        } else {
                            imageQuestionsState.setImageResource(R.drawable.ic_wrong_answer)
                            textQuestionState.text = "Đáp án sai"
                        }
                    }

                    viewQuestionExplain.isVisible = true

                    questionOptionAdapter.disableClickEvent()

                    root.setOnClickListener {
                        notifyItemChanged(adapterPosition)
                        elem.isVisible = elem.isVisible.not()
                        elem.visibleWithAnimation()
                        updateCallback?.invoke(adapterPosition)
                    }

                    buttonSpanContent.setOnClickListener {
                        notifyItemChanged(adapterPosition)
                        elem.isVisible = elem.isVisible.not()
                        elem.visibleWithAnimation()
                        updateCallback?.invoke(adapterPosition)
                    }
                }
            }
        }
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<NewQuestionWithState>) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemExamQuestionResultScreenBinding::inflate, parent))
    }
}