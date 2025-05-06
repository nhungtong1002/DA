package com.example.applearndriver.di

import com.example.applearndriver.data.datasource.IExamDataSource
import com.example.applearndriver.data.datasource.IStudyDataSource
import com.example.applearndriver.data.datasource.ITipsHighScoreDataSource
import com.example.applearndriver.data.datasource.ITrafficSignalDataSource
import com.example.applearndriver.data.datasource.IWrongAnswerDataSource
import com.example.applearndriver.data.repository.ExamRepository
import com.example.applearndriver.data.repository.StudyRepository
import com.example.applearndriver.data.repository.TipsHighScoreRepository
import com.example.applearndriver.data.repository.TrafficRepository
import com.example.applearndriver.data.repository.WrongAnswerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideExamRepository(
        localDataSource: IExamDataSource.Local,
        remoteDataSource: IExamDataSource.Remote
    ): ExamRepository {
        return ExamRepository(local = localDataSource, remote = remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideStudyRepository(
        localDataSource: IStudyDataSource.Local,
        remoteDataSource: IStudyDataSource.Remote
    ): StudyRepository {
        return StudyRepository(local = localDataSource, remote = remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideTipsHighScoreRepository(
        remoteDataSource: ITipsHighScoreDataSource.Remote
    ): TipsHighScoreRepository {
        return TipsHighScoreRepository(remote = remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideTrafficRepository(
        remoteDataSource: ITrafficSignalDataSource.Remote
    ): TrafficRepository {
        return TrafficRepository(remote = remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideWrongAnswerRepository(
        localDataSource: IWrongAnswerDataSource.Local,
        remoteDataSource: IWrongAnswerDataSource.Remote
    ): WrongAnswerRepository {
        return WrongAnswerRepository(local = localDataSource, remote = remoteDataSource)
    }
}