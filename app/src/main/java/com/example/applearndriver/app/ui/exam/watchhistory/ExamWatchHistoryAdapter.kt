package com.example.applearndriver.app.ui.exam.watchhistory

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.core.text.bold
import com.example.applearndriver.R
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.data.model.ExamHistory
import com.example.applearndriver.data.model.ExamState
import com.example.applearndriver.databinding.ItemExamResultHistoryBinding
import com.example.applearndriver.utils.OnClickItem
import com.example.applearndriver.utils.extensions.getResourceColor

class ExamWatchHistoryAdapter :
    BaseRecyclerViewAdapter<ExamHistory, ItemExamResultHistoryBinding, ExamWatchHistoryAdapter.ViewHolder>(ExamHistory.getDiffCallBack()) {

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<ExamHistory>) {
        //Not implement
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(ItemExamResultHistoryBinding::inflate, parent))
    }

    inner class ViewHolder(binding: ItemExamResultHistoryBinding)
        : BaseViewHolder<ExamHistory, ItemExamResultHistoryBinding>(binding) {
        override fun onBindData(data: ExamHistory) {
            binding.apply {
                textExamTimes.text = "Lần ${data.times}"
                textExamNumbersCorrectAnswer.text = "Số câu làm đúng: ${data.numbersOfCorrectAnswer}/${data.totalNumbersOfQuestion} câu"
                textExamResult.text = SpannableStringBuilder().apply {
                    append("Kết quả thi: ")
                    bold {
                        if(data.examState == ExamState.PASSED.value) {
                            append(SpannableString("ĐẠT").apply {
                               setSpan(
                                   ForegroundColorSpan(binding.root.context.getResourceColor(R.color.green_pastel)),
                                   0,
                                   length,
                                   Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                               )
                            })
                        } else {
                            append(SpannableString("KHÔNG ĐẠT").apply {
                                setSpan(
                                    ForegroundColorSpan(binding.root.context.getResourceColor(R.color.red_pastel)),
                                    0,
                                    length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            })
                        }
                    }
                }

                textExamDescription.text = data.description
            }
        }
    }
}