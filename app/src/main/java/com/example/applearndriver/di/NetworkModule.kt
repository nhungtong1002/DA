package com.example.applearndriver.di

import android.content.Context
import com.example.applearndriver.utils.network.InternetConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkObserver(@ApplicationContext context: Context): InternetConnection {
        return InternetConnection(context)
    }
}