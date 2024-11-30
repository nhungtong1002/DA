package com.example.applearndriver.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

   /* @Provides
    @Singleton
    fun provideTodoRepository(sampleDataSource: SampleDataSource.Local): SampleRepository {
        return SampleRepositoryImpl(sampleDataSource)
    }*/
}