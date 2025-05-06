package com.example.applearndriver.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applearndriver.data.model.NewQuestion

@Dao
interface QuestionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewQuestions(vararg questions: NewQuestion)

    @Delete
    suspend fun deleteQuestions(vararg questions: NewQuestion)

    @Query("select * from QUESTIONS")
    suspend fun getAllQuestions(): MutableList<NewQuestion>

    @Query("select * from QUESTIONS where id=:id")
    suspend fun getDetailQuestionsByID(id: Int): NewQuestion

}
