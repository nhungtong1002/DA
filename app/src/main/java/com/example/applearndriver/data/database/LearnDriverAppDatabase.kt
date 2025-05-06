package com.example.applearndriver.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.applearndriver.data.database.LearnDriverAppDatabase.Companion.ALLOW_EXPORT_SCHEMA
import com.example.applearndriver.data.database.LearnDriverAppDatabase.Companion.DATABASE_VERSION
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.StudyCategory
import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.data.model.roomtypeconverter.ExamTypeConverter
import com.example.applearndriver.data.model.roomtypeconverter.QuestionOptionTypeConverter
import com.example.applearndriver.data.model.roomtypeconverter.QuestionTypeConverter
import com.example.applearndriver.data.model.roomtypeconverter.WrongAnswerObjectTypeConverter

@Database(
    entities = [
        Exam::class,
        NewQuestion::class,
        StudyCategory::class,
        WrongAnswer::class,
    ],
    version = DATABASE_VERSION,
    exportSchema = ALLOW_EXPORT_SCHEMA,
)
@TypeConverters(
    ExamTypeConverter::class,
    QuestionTypeConverter::class,
    QuestionOptionTypeConverter::class,
    WrongAnswerObjectTypeConverter::class,
)
abstract class LearnDriverAppDatabase : RoomDatabase() {
    abstract fun getExamDao(): ExamDao
    abstract fun getQuestionDao(): QuestionsDao
    abstract fun getStudyDao(): StudyDao
    abstract fun getWrongAnswerDao(): WrongAnswerDao

    companion object {
        const val DATABASE_NAME = "MotorbikeAppDatabase"
        const val DATABASE_VERSION = 1
        const val ALLOW_EXPORT_SCHEMA = false
    }
}
