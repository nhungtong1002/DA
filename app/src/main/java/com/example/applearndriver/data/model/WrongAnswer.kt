package com.example.applearndriver.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applearndriver.data.model.WrongAnswer.Companion.WRONG_ANSWER_TABLE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = WRONG_ANSWER_TABLE)
data class WrongAnswer(
    @PrimaryKey val questionsID: Int,
    val lastWrongTime: Long,
    val lastSelectedState: QuestionOptions,
) : Parcelable {
    companion object {
        const val WRONG_ANSWER_TABLE = "WRONG_ANSWER_TABLE"
    }
}
