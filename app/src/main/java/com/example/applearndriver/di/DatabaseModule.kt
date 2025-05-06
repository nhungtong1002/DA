package com.example.applearndriver.di

import android.content.Context
import androidx.room.Room
import com.example.applearndriver.data.database.ExamDao
import com.example.applearndriver.data.database.LearnDriverAppDatabase
import com.example.applearndriver.data.database.QuestionsDao
import com.example.applearndriver.data.database.StudyDao
import com.example.applearndriver.data.database.WrongAnswerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LearnDriverAppDatabase {
        return Room.databaseBuilder(
            context,
            LearnDriverAppDatabase::class.java,
            "appDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideExamDao(database: LearnDriverAppDatabase): ExamDao {
        return database.getExamDao()
    }
    @Provides
    fun provideQuestionDao(database: LearnDriverAppDatabase):  QuestionsDao{
        return database.getQuestionDao()
    }
    @Provides
    fun provideStudyDao(database: LearnDriverAppDatabase):  StudyDao{
        return database.getStudyDao()
    }
    @Provides
    fun provideWrongAnswerDao(database: LearnDriverAppDatabase):  WrongAnswerDao{
        return database.getWrongAnswerDao()
    }


}