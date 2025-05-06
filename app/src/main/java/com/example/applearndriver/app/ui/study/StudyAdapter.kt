package com.example.applearndriver.app.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.applearndriver.base.BaseRecyclerViewAdapter
import com.example.applearndriver.base.BaseViewHolder
import com.example.applearndriver.data.model.StudyCategory
import com.example.applearndriver.databinding.ItemFragmentStudyLayoutBinding
import com.example.applearndriver.utils.OnClickItem

class StudyAdapter
    : BaseRecyclerViewAdapter<StudyCategory, ItemFragmentStudyLayoutBinding,
        StudyAdapter.ViewHolder>(StudyCategory.getDiffUtilCallBack()) {

    private var onClickPosition: ((Int) -> Unit)? = null

    fun registerOnClickPositionEvent(onClickItemPosition: (Int) -> Unit) {
        this.onClickPosition = onClickItemPosition
    }

    override fun registerOnClickItemEvent(onClickItem: OnClickItem<StudyCategory>) {
        this.clickItemInterface = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFragmentStudyLayoutBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(
        override val binding: ItemFragmentStudyLayoutBinding,
    ) : BaseViewHolder<StudyCategory, ItemFragmentStudyLayoutBinding>(binding) {
        override fun onBindData(data: StudyCategory) {
            binding.apply {
                imageQuestionsCategoryBanner.setImageResource(StudyViewModel.listDefaultStudyCategory[adapterPosition].iconResourceID)
                textQuestionCategory.text = data.title
                textNumbersOfQuestion.text = "${data.totalNumberOfQuestions} câu"
                textNumbersOfSelectedAnswerQuestion.text =
                    "${data.numbersOfSelectedQuestions}/${data.totalNumberOfQuestions}"
                progressBarNumbersOfSelectedAnswerQuestion.progress =
                    calculateProgress(data.numbersOfSelectedQuestions, data.totalNumberOfQuestions)

                root.setOnClickListener {
                    onClickPosition?.let { function -> function(adapterPosition) }
                }
            }
        }

        private fun calculateProgress(done: Int, total: Int) =
            (done.toFloat() / total.toFloat() * ONE_HUNDRED_PERCENT).toInt()
    }

    companion object {
        private const val ONE_HUNDRED_PERCENT = 100F
    }
}
