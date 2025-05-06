package com.nguyennhatminh614.motobikedriverlicenseapp.screen.exam.watchhistory

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.core.text.bold
import com.nguyennhatminh614.motobikedriverlicenseapp.R
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamHistory
import com.nguyennhatminh614.motobikedriverlicenseapp.data.model.ExamState
import com.nguyennhatminh614.motobikedriverlicenseapp.databinding.ItemExamResultHistoryBinding
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.OnClickItem
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseRecyclerViewAdapter
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.base.BaseViewHolder
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.extensions.getResourceColor

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