package com.example.applearndriver.app.ui.appadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.databinding.ItemOptionsQuestionLayoutBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.extensions.getSelectedColor
import com.example.applearndriver.utils.extensions.isCurrentDarkModeByDefault

class QuestionOptionAdapter :
    BaseRecyclerViewAdapter<QuestionOptions, ItemOptionsQuestionLayoutBinding, QuestionOptionAdapter.ViewHolder>(
        QuestionOptions.getDiffUtilCallBack()
    ) {

    private var selectedPosition = AppConstant.NONE_POSITION
    private var isClickable = true

    private var correctAnswerPositionExamResult = AppConstant.NONE_POSITION
    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionOptions>) {
        this.clickItemInterface = onClickItem
    }

    fun disableClickEvent() {
        isClickable = false
    }

    fun updateStateListWithPosition(item: MutableList<QuestionOptions>, position: Int) {
        setSingleSelection(position)
        submitList(item)
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOptionsQuestionLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    private fun setSingleSelection(adapterPosition: Int) {
        selectedPosition = adapterPosition
    }

    fun updateCorrectAnswerPosition(correctAnswerPosition: Int) {
        correctAnswerPositionExamResult = correctAnswerPosition
        notifyItemChanged(correctAnswerPosition)
    }

    companion object {
        val UNSELECTED_COLOR = R.color.white
        val SELECTED_COLOR = R.color.highlight_color
        val TEXT_COLOR = R.color.black
        val STATE_INCORRECT_COLOR = R.color.red_pastel
        val STATE_CORRECT_COLOR = R.color.green_pastel
        val DARK_MODE_BACKGROUND_UNSELECTED_COLOR = R.color.grey_700
        val VIEW_POSITION_LAYOUT_BACKGROUND_COLOR = R.color.white
    }

    inner class ViewHolder(
        override val binding: ItemOptionsQuestionLayoutBinding,
    ) : BaseViewHolder<QuestionOptions, ItemOptionsQuestionLayoutBinding>(binding) {
        override fun onBindData(item: QuestionOptions) {
            binding.apply {
                val currentDisplayPosition = adapterPosition + 1
                textQuestionOptions.text = item.title
                textItemPosition.text = currentDisplayPosition.toString()

                if (selectedPosition == adapterPosition) {
                    val currentColor = when (item.stateNumber) {
                        StateQuestionOption.UNKNOWN.type -> getSelectedColor(SELECTED_COLOR)
                        StateQuestionOption.INCORRECT.type -> getSelectedColor(STATE_INCORRECT_COLOR)
                        StateQuestionOption.CORRECT.type -> getSelectedColor(STATE_CORRECT_COLOR)
                        else -> getSelectedColor(UNSELECTED_COLOR)
                    }
                    layoutQuestionItem.setCardBackgroundColor(currentColor)
                } else {
                    if (isCurrentDarkModeByDefault) {
                        layoutQuestionItem.setCardBackgroundColor(
                            getSelectedColor(
                                DARK_MODE_BACKGROUND_UNSELECTED_COLOR
                            )
                        )
                    } else {
                        layoutQuestionItem.setCardBackgroundColor(getSelectedColor(UNSELECTED_COLOR))
                    }
                }

                if (correctAnswerPositionExamResult != AppConstant.NONE_POSITION
                    && correctAnswerPositionExamResult == adapterPosition
                ) {
                    layoutQuestionItem.setCardBackgroundColor(getSelectedColor(STATE_CORRECT_COLOR))
                }

                viewPositionLayout.setCardBackgroundColor(
                    getSelectedColor(
                        VIEW_POSITION_LAYOUT_BACKGROUND_COLOR
                    )
                )
                textQuestionOptions.setTextColor(getSelectedColor(TEXT_COLOR))
                textItemPosition.setTextColor(getSelectedColor(TEXT_COLOR))

                if (isClickable) {
                    root.setOnClickListener {
                        setSingleSelection(adapterPosition)
                        clickItemInterface?.let { function ->
                            function(
                                QuestionOptions(
                                    questionsID = item.questionsID,
                                    position = adapterPosition,
                                    title = item.title,
                                    isSelected = true,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
