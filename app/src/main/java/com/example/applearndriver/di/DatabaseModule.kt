package com.example.applearndriver.di

import android.content.Context
import androidx.room.Room
import com.example.applearndriver.data.database.LearnDriverAppDatabase
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
    private fun provideDatabase(@ApplicationContext context: Context): LearnDriverAppDatabase {
        return Room.databaseBuilder(
            context,
            LearnDriverAppDatabase::class.java,
            "appDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }
}