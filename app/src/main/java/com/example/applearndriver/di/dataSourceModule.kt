package com.example.applearndriver.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

   /* @Provides
    @Singleton
    fun provideLocalDataSource(sampleDao: SampleDao): SampleDataSource.Local {
        return SampleLocalDataSourceImpl(sampleDao)
    }*/
}