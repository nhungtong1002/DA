package com.example.applearndriver.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applearndriver.data.model.NewQuestion.Companion.TABLE_QUESTION
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_QUESTION)
class NewQuestion : Parcelable {
    @PrimaryKey
    var id = DEFAULT_ID
    var question = ""
    var listOption = mutableListOf<String>()
    var correctAnswerPosition: Int = 1
    var isImmediateFailedTestWhenWrong: Boolean = false

    @set:PropertyName("image")
    @get:PropertyName("image")
    @SerializedName("image")
    var image = ""

    var hasImageBanner = false
    var explain = ""

    @set:PropertyName("question_type")
    @get:PropertyName("question_type")
    @SerializedName("question_type")
    var questionType = ""

    var minimumLicenseType = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewQuestion

        if (id != other.id) return false
        if (question != other.question) return false
        if (listOption != other.listOption) return false
        if (correctAnswerPosition != other.correctAnswerPosition) return false
        if (isImmediateFailedTestWhenWrong != other.isImmediateFailedTestWhenWrong) return false
        if (image != other.image) return false
        if (hasImageBanner != other.hasImageBanner) return false
        if (explain != other.explain) return false
        if (questionType != other.questionType) return false
        if (minimumLicenseType != other.minimumLicenseType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + question.hashCode()
        result = 31 * result + listOption.hashCode()
        result = 31 * result + correctAnswerPosition
        result = 31 * result + isImmediateFailedTestWhenWrong.hashCode()
        result = 31 * result + hasImageBanner.hashCode()
        result = 31 * result + explain.hashCode()
        result = 31 * result + minimumLicenseType.hashCode()
        return result
    }

    override fun toString(): String {
        return "NewQuestion(id=$id, question='$question', listOption=$listOption, correctAnswerPosition=$correctAnswerPosition, isImmediateFailedTestWhenWrong=$isImmediateFailedTestWhenWrong, image='$image', hasImageBanner=$hasImageBanner, explain='$explain', questionType='$questionType', minimumLicenseType='$minimumLicenseType')"
    }

    companion object {
        const val TABLE_QUESTION = "QUESTIONS"
        const val DEFAULT_ID = 1
        fun getDiffCallBack() = object : DiffUtil.ItemCallback<NewQuestion>() {
            override fun areItemsTheSame(
                oldItem: NewQuestion,
                newItem: NewQuestion,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: NewQuestion,
                newItem: NewQuestion,
            ): Boolean = oldItem == newItem
        }
    }
}

class NewQuestionWithState(
    val newQuestion: NewQuestion,
    var isVisible: Boolean = false
) {

    companion object {
        fun getDiffCallBack() = object : DiffUtil.ItemCallback<NewQuestionWithState>() {
            override fun areItemsTheSame(
                oldItem: NewQuestionWithState,
                newItem: NewQuestionWithState,
            ): Boolean = oldItem.newQuestion.id == newItem.newQuestion.id

            override fun areContentsTheSame(
                oldItem: NewQuestionWithState,
                newItem: NewQuestionWithState,
            ): Boolean = oldItem == newItem
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewQuestionWithState

        if (newQuestion != other.newQuestion) return false
        if (isVisible != other.isVisible) return false

        return true
    }

    override fun hashCode(): Int {
        var result = newQuestion.hashCode()
        result = 31 * result + isVisible.hashCode()
        return result
    }
}

enum class QuestionType(
    val type: String,
    val title: String,
    val position: Int,
    val startIDQuestion: Int,
    val endIDQuestion: Int
) {
    ALL(
        type = "All",
        title = "Tất cả câu hỏi",
        position = 0,
        startIDQuestion = 1,
        endIDQuestion = 600,
    ),
    TRAFFIC_CONCEPT_AND_RULES(
        type = "TrafficConceptAndRules",
        title = "Khái niệm và quy tắc giao thông",
        position = 1,
        startIDQuestion = 1,
        endIDQuestion = 166
    ),
    DRIVING_BUSINESS(
        type = "DrivingBusiness",
        title = "Nghiệp vụ vận tải",
        position = 2,
        startIDQuestion = 167,
        endIDQuestion = 192
    ),
    ETHICS_IN_DRIVING(
        type = "EthicsInDriving",
        title = "Văn hóa, đạo đức khi lái xe",
        position = 3,
        startIDQuestion = 193,
        endIDQuestion = 213
    ),
    DRIVING_TECHNIQUE(
        type = "DrivingTechnique",
        title = "Kỹ thuật lái xe",
        position = 4,
        startIDQuestion = 214,
        endIDQuestion = 269
    ),
    FIXING_CAR_QUESTION(
        type = "FixingCarQuestion",
        title = "Cấu tạo và sửa chữa xe",
        position = 5,
        startIDQuestion = 270,
        endIDQuestion = 304
    ),
    TRAFFIC_SIGNAL(
        type = "TrafficSignal",
        title = "Hệ thống biển báo",
        position = 6,
        startIDQuestion = 305,
        endIDQuestion = 486
    ),
    TRAFFIC_SITUATION(
        type = "TrafficSituation",
        title = "Giải các thế sa hình",
        position = 7,
        startIDQuestion = 487,
        endIDQuestion = 600
    ),
}