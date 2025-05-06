package com.example.applearndriver.di

import com.example.applearndriver.data.database.ExamDao
import com.example.applearndriver.data.database.StudyDao
import com.example.applearndriver.data.database.WrongAnswerDao
import com.example.applearndriver.data.datasource.IExamDataSource
import com.example.applearndriver.data.datasource.IStudyDataSource
import com.example.applearndriver.data.datasource.ITipsHighScoreDataSource
import com.example.applearndriver.data.datasource.ITrafficSignalDataSource
import com.example.applearndriver.data.datasource.IWrongAnswerDataSource
import com.example.applearndriver.data.datasource.local.exam.LocalExamDataSource
import com.example.applearndriver.data.datasource.local.study.LocalStudyDataSource
import com.example.applearndriver.data.datasource.local.wronganswer.LocalWrongAnswerDataSource
import com.example.applearndriver.data.datasource.remote.exam.RemoteExamDataSource
import com.example.applearndriver.data.datasource.remote.study.RemoteStudyDataSource
import com.example.applearndriver.data.datasource.remote.tipshighscore.RemoteTipsHighScoreDataSource
import com.example.applearndriver.data.datasource.remote.trafficsign.TrafficSignRemoteDataSource
import com.example.applearndriver.data.datasource.remote.wronganswer.WrongAnswerRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalExamDataSource(examDao: ExamDao): IExamDataSource.Local {
        return LocalExamDataSource(examDao)
    }

    @Provides
    @Singleton
    fun provideRemoteExamDataSource(firestore: FirebaseFirestore): IExamDataSource.Remote {
        return RemoteExamDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideLocalStudyDataSource(studyDao: StudyDao): IStudyDataSource.Local {
        return LocalStudyDataSource(studyDao)
    }

    @Provides
    @Singleton
    fun provideRemoteStudyDataSource(firestore: FirebaseFirestore): IStudyDataSource.Remote {
        return RemoteStudyDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideLocalWrongAnswerDataSource(wrongAnswerDao: WrongAnswerDao): IWrongAnswerDataSource.Local {
        return LocalWrongAnswerDataSource(wrongAnswerDao)
    }

    @Provides
    @Singleton
    fun provideRemoteWrongAnswerDataSource(firestore: FirebaseFirestore): IWrongAnswerDataSource.Remote {
        return WrongAnswerRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideRemoteTipsHighScoreDataSource(firestore: FirebaseFirestore): ITipsHighScoreDataSource.Remote {
        return RemoteTipsHighScoreDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideRemoteTrafficSignalDataSource(firestore: FirebaseFirestore): ITrafficSignalDataSource.Remote {
        return TrafficSignRemoteDataSource(firestore)
    }
}