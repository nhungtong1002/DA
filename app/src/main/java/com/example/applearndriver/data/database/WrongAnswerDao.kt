package com.example.applearndriver.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.applearndriver.data.model.WrongAnswer

@Dao
interface WrongAnswerDao {

    @Query("select * from wrong_answer_table order by lastWrongTime desc")
    suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer>

    @Query("select * from wrong_answer_table where questionsID = :id")
    suspend fun findWrongAnswerQuestionByID(id: Int): WrongAnswer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewWrongAnswerQuestion(vararg wrongAnswer: WrongAnswer)

    @Update
    suspend fun updateWrongAnswerQuestion(vararg wrongAnswer: WrongAnswer)
}
