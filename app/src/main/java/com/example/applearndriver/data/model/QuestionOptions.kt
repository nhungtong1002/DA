package com.example.applearndriver.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionOptions(
    val questionsID: Int,
    val position: Int,
    val title: String,
    val isSelected: Boolean = false,
    var stateNumber: Int = StateQuestionOption.UNKNOWN.type,
) : Parcelable {
    companion object {
        fun getDiffUtilCallBack() = object : DiffUtil.ItemCallback<QuestionOptions>() {
            override fun areItemsTheSame(
                oldItem: QuestionOptions,
                newItem: QuestionOptions,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: QuestionOptions,
                newItem: QuestionOptions,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

enum class StateQuestionOption(val type: Int) {
    UNKNOWN(Constant.UNKNOWN_TYPE),
    CORRECT(Constant.CORRECT_TYPE),
    INCORRECT(Constant.INCORRECT_TYPE);

    object Constant {
        const val UNKNOWN_TYPE = 0
        const val CORRECT_TYPE = 1
        const val INCORRECT_TYPE = 2
    }
}
