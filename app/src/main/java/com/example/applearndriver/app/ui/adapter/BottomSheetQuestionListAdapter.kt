package com.example.applearndriver.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.constant.AppConstant.NONE_POSITION
import com.example.applearndriver.data.model.QuestionOptions
import com.example.applearndriver.data.model.StateQuestionOption
import com.example.applearndriver.databinding.ItemExamQuestionBottomSheetDialogBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.OnClickItemPosition
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getResourceColor

class BottomSheetQuestionListAdapter :
    ListAdapter<QuestionOptions, BaseViewHolder<ItemExamQuestionBottomSheetDialogBinding>>(DIFF) {
    private var currentQuestionIndex = NONE_POSITION
    private var onClickItemPosition: OnClickItemPosition<QuestionOptions>? = null
    private var isExamScreen = false
    private var clickItem: OnClickItem<QuestionOptions>? = null

    companion object {
        val SELECTED_COLOR = R.color.primary_color
        val UNSELECTED_COLOR = R.color.transparent
        val STATE_UNKNOWN_COLOR = R.color.white
        val STATE_INCORRECT_COLOR = R.color.red_pastel
        val STATE_CORRECT_COLOR = R.color.green_pastel
        private val DIFF = object : DiffUtil.ItemCallback<QuestionOptions>() {
            override fun areItemsTheSame(
                oldItem: QuestionOptions, newItem: QuestionOptions,
            ): Boolean {
                return oldItem.questionsID == newItem.questionsID
            }

            override fun areContentsTheSame(
                oldItem: QuestionOptions, newItem: QuestionOptions,
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ItemExamQuestionBottomSheetDialogBinding> {
        return BaseViewHolder(
            ItemExamQuestionBottomSheetDialogBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ItemExamQuestionBottomSheetDialogBinding>,
        position: Int,
    ) {
        val item = getItem(position)
        holder.binding.apply {
            val currentPosition = (position + 1).toString()
            val currentQuestionOptionSelectedPos = (item.position + 1).toString()

            textQuestionIndex.text = if(isExamScreen) currentPosition else item.questionsID.toString()

            if (item.position != NONE_POSITION) {
                itemBadgeLayout.isVisible = true
                textSelectionState.text = currentQuestionOptionSelectedPos
            } else {
                itemBadgeLayout.isVisible = false
            }

            when (item.stateNumber) {
                StateQuestionOption.UNKNOWN.type -> {
                    itemQuestionIcon.setCardBackgroundColor(getSelectedColor(holder.binding,
                        STATE_UNKNOWN_COLOR))
                }
                StateQuestionOption.INCORRECT.type -> {
                    itemQuestionIcon.setCardBackgroundColor(getSelectedColor(holder.binding,
                        STATE_INCORRECT_COLOR))
                }
                StateQuestionOption.CORRECT.type -> {
                    itemQuestionIcon.setCardBackgroundColor(getSelectedColor(holder.binding,
                        STATE_CORRECT_COLOR))
                }
            }

            root.setOnClickListener {
                onClickItemPosition?.let { function -> function(position, item) }
            }
        }

        if (currentQuestionIndex == position) {
            holder.binding.itemQuestionIcon.strokeColor =
                getSelectedColor(holder.binding, SELECTED_COLOR)
        } else {
            holder.binding.itemQuestionIcon.strokeColor =
                getSelectedColor(holder.binding, UNSELECTED_COLOR)
        }
    }

    private fun getSelectedColor(binding: ItemExamQuestionBottomSheetDialogBinding, color: Int) =
        binding.root.context.getResourceColor(color)
    fun setSingleSelection(adapterPosition: Int) {
        currentQuestionIndex = adapterPosition
        submitList(currentList)
        notifyDataSetChanged()
    }
    fun registerOnClickItemEvent(onClickItem: OnClickItem<QuestionOptions>) {
        this.clickItem = onClickItem
    }
    fun examScreenMode() {
        isExamScreen = true
        notifyDataSetChanged()
    }

    fun registerOnClickItemPositionEvent(onClickItem: OnClickItemPosition<QuestionOptions>) {
        this.onClickItemPosition = onClickItem
    }
}
