package com.example.applearndriver.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.model.Exam.Companion.TABLE_EXAM
import com.example.applearndriver.data.model.ExamState.Constant.FAILED_BY_MUST_NOT_WRONG_QUESTION_TYPE
import com.example.applearndriver.data.model.ExamState.Constant.FAILED_BY_N0T_ENOUGH_SCORE_TYPE
import com.example.applearndriver.data.model.ExamState.Constant.PASSED_TYPE
import com.example.applearndriver.data.model.ExamState.Constant.UNDEFINED_TYPE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_EXAM)
data class Exam(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val listQuestions: MutableList<NewQuestion> = mutableListOf(),
    var numbersOfCorrectAnswer: Int = DEFAULT_NOT_START_EXAM_CORRECT_ANSWER_COUNT,
    var currentTimeStamp: Long = AppConstant.DEFAULT_NOT_HAVE_TIME_STAMP,
    var timeExamDone: Long = 0,
    var listQuestionOptions: MutableList<QuestionOptions> = mutableListOf(),
    var examState: String = ExamState.UNDEFINED.value,
    val examType: String,
    val listExamHistory: MutableList<ExamHistory> = mutableListOf(),
) : Parcelable {

    companion object {
        const val TABLE_EXAM = "EXAM"
        const val DEFAULT_NOT_START_EXAM_CORRECT_ANSWER_COUNT = 0

        fun getDiffCallBack() = object : DiffUtil.ItemCallback<Exam>() {
            override fun areItemsTheSame(
                oldItem: Exam,
                newItem: Exam,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Exam,
                newItem: Exam,
            ): Boolean = oldItem == newItem
        }
    }
}

enum class ExamState(val value: String) {
    UNDEFINED(UNDEFINED_TYPE),
    PASSED(PASSED_TYPE),
    FAILED_BY_N0T_ENOUGH_SCORE(FAILED_BY_N0T_ENOUGH_SCORE_TYPE),
    FAILED_BY_MUST_NOT_WRONG_QUESTION(FAILED_BY_MUST_NOT_WRONG_QUESTION_TYPE);
    object Constant {
        const val UNDEFINED_TYPE = "undefined"
        const val PASSED_TYPE = "passed"
        const val FAILED_BY_N0T_ENOUGH_SCORE_TYPE = "failed_not_enough_score"
        const val FAILED_BY_MUST_NOT_WRONG_QUESTION_TYPE = "failed_by_must_not_wrong_question"
    }
}

@Parcelize
data class ExamHistory(
    val times: Int,
    val numbersOfCorrectAnswer: Int,
    val totalNumbersOfQuestion: Int,
    val timeExamDone: Long,
    val examState: String,
    var description: String = "",
): Parcelable {
    companion object {
        fun getDiffCallBack() = object : DiffUtil.ItemCallback<ExamHistory>() {
            override fun areItemsTheSame(
                oldItem: ExamHistory,
                newItem: ExamHistory,
            ): Boolean = oldItem.times == newItem.times

            override fun areContentsTheSame(
                oldItem: ExamHistory,
                newItem: ExamHistory,
            ): Boolean = oldItem == newItem
        }
    }
}
