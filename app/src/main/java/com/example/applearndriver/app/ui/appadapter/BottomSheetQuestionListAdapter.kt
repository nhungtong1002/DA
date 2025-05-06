package com.example.applearndriver.app.ui.appadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.constant.AppConstant.NONE_POSITION
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.databinding.ItemExamQuestionBottomSheetDialogBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.OnClickItemPosition
import com.example.applearndriver.utils.extensions.getResourceColor

class BottomSheetQuestionListAdapter :
    BaseRecyclerViewAdapter<QuestionOptions, ItemExamQuestionBottomSheetDialogBinding,
            BottomSheetQuestionListAdapter.ViewHolder>(
        QuestionOptions.getDiffUtilCallBack()
    ) {

    private var currentQuestionIndex = NONE_POSITION
    private var onClickItemPosition: OnClickItemPosition<QuestionOptions>? = null

    private var isExamScreen = false

    fun examScreenMode() {
        isExamScreen = true
        notifyDataSetChanged()
    }

    fun registerOnClickItemPositionEvent(onClickItem: OnClickItemPosition<QuestionOptions>) {
        this.onClickItemPosition = onClickItem
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionOptions>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemExamQuestionBottomSheetDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)
    }

    private fun getSelectedColor(binding: ItemExamQuestionBottomSheetDialogBinding, color: Int) =
        binding.root.context.getResourceColor(color)

    fun setSingleSelection(adapterPosition: Int) {
        currentQuestionIndex = adapterPosition
        submitList(currentList)
        notifyDataSetChanged()
    }

    companion object {
        val SELECTED_COLOR = R.color.primary_color
        val UNSELECTED_COLOR = R.color.transparent
        val STATE_UNKNOWN_COLOR = R.color.white
        val STATE_INCORRECT_COLOR = R.color.red_pastel
        val STATE_CORRECT_COLOR = R.color.green_pastel
    }

    inner class ViewHolder(
        override val binding: ItemExamQuestionBottomSheetDialogBinding,
    ) : BaseViewHolder<QuestionOptions, ItemExamQuestionBottomSheetDialogBinding>(binding) {
        override fun onBindData(item: QuestionOptions) {
            binding.apply {
                val currentPosition = (adapterPosition + 1).toString()
                val currentQuestionOptionSelectedPos = (item.position + 1).toString()

                textQuestionIndex.text =
                    if (isExamScreen) currentPosition else item.questionsID.toString()

                if (item.position != NONE_POSITION) {
                    itemBadgeLayout.isVisible = true
                    textSelectionState.text = currentQuestionOptionSelectedPos
                } else {
                    itemBadgeLayout.isVisible = false
                }

                when (item.stateNumber) {
                    StateQuestionOption.UNKNOWN.type -> {
                        itemQuestionIcon.setCardBackgroundColor(
                            getSelectedColor(
                                binding,
                                STATE_UNKNOWN_COLOR
                            )
                        )
                    }

                    StateQuestionOption.INCORRECT.type -> {
                        itemQuestionIcon.setCardBackgroundColor(
                            getSelectedColor(
                                binding,
                                STATE_INCORRECT_COLOR
                            )
                        )
                    }

                    StateQuestionOption.CORRECT.type -> {
                        itemQuestionIcon.setCardBackgroundColor(
                            getSelectedColor(
                                binding,
                                STATE_CORRECT_COLOR
                            )
                        )
                    }
                }

                root.setOnClickListener {
                    onClickItemPosition?.let { function -> function(adapterPosition, item) }
                }
            }

            if (currentQuestionIndex == adapterPosition) {
                binding.itemQuestionIcon.strokeColor =
                    getSelectedColor(binding, SELECTED_COLOR)
            } else {
                binding.itemQuestionIcon.strokeColor =
                    getSelectedColor(binding, UNSELECTED_COLOR)
            }
        }
    }
}
